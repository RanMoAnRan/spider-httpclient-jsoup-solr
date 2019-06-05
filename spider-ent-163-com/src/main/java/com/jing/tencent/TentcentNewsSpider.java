package com.jing.tencent;

import com.google.gson.Gson;
import com.jing.dao.NewsDao;
import com.jing.pojo.News;
import com.jing.utils.HttpClientUtils;
import com.jing.utils.IdWorker;
import com.jing.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:TentcentNewsSpider
 * @Description TODO 爬取腾讯娱乐新闻
 * @author:RanMoAnRan
 * @Date:2019/6/3 19:25
 * @Version 1.0
 */
public class TentcentNewsSpider {
    private static IdWorker idWorker = new IdWorker(0, 1);
    private static NewsDao newsDao = new NewsDao();

    public static void main(String[] args) throws IOException {
        //热点新闻url
        String hotUrl = "https://pacaio.match.qq.com/irs/rcd?cid=137&token=d0f13d594edfc180f5bf6b845456f3ea&ext=ent&num=60";
        //非热点新闻首页url
        String noHotUrl = "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page=0";
        //非热点新闻下一页url
        String nexturl = "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page=1";
        page(hotUrl, noHotUrl);
    }

    //解析json数据
    public static List<News> parseNewsJson(String url) {
        Gson gson = new Gson();
        Map<String, Object> urlMap = gson.fromJson(url, Map.class);
        Double datanum = (Double) urlMap.get("datanum");
        Integer num = datanum.intValue();
        System.out.println(num);
        if (num == 0 || num == null) {
            //没数据返回空
            return null;
        }
        // 获取 列表页新闻数据
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) urlMap.get("data");

        List<News> newsList = new ArrayList<>();
        //System.out.println(dataList);
        //每个newMap都是一条完整的新闻数据
        for (Map<String, Object> newMap : dataList) {

            String docurl = (String) newMap.get("vurl");
            if (docurl.contains("video")) continue;

            //redis去重判断
            Jedis jedis = JedisUtils.getJedis();
            Boolean flag = jedis.sismember("tencent-news", docurl);
            jedis.close();
            if (flag) {
                //如果存在代表已经爬取过此url
                continue;
            }

            News news = new News();
            //id
            long id = idWorker.nextId();
            news.setId(id + "");
            //title
            String title = (String) newMap.get("title");
            //System.out.println(title);
            news.setTitle(title);
            //time
            String time = (String) newMap.get("update_time");
            news.setTime(time);
            //新闻的source
            String source = (String) newMap.get("source");
            news.setSource(source);
            //content
            String content = (String) newMap.get("intro");
            news.setContent(content);
            //editor
            news.setEditor(source);
            //docurl
            news.setDocurl(docurl);
            //System.out.println(news);
            newsList.add(news);
        }
        return newsList;
    }

    //保存数据
    public static void saveNews(List<News> newsList) {
        Jedis jedis = JedisUtils.getJedis();
        for (News news : newsList) {

            //redis去重判断
            Boolean flag = jedis.sismember("tencent-news", news.getDocurl());
            if (flag) {
                //如果存在代表已经爬取过此url
                continue;
            }

            System.out.println(news.getDocurl());
            System.out.println(news);
            newsDao.saveBeans(news);
            //保存数据后将已经爬取过的url存入redis中
            jedis.sadd("tencent-news", news.getDocurl());
        }
        jedis.close();
    }

    //获取非热点下一页数据
    public static void page(String hotUrl, String noHotUrl) throws IOException {
        Integer page = 1;

        //发送请求，获取数据
        String hotUrlJson = HttpClientUtils.doGet(hotUrl);
        //解析热点数据
        List<News> hotNewsList = parseNewsJson(hotUrlJson);

        //保存热点数据
        saveNews(hotNewsList);

        while (true) {
            //发送请求，获取数据
            String noHotUrlJson = HttpClientUtils.doGet(noHotUrl);
            //解析非热点数据
            List<News> noHotNewsList = parseNewsJson(noHotUrlJson);

            if (noHotNewsList == null) break;

            //保存非热点数据
            saveNews(noHotNewsList);

            //获取非热点的下一页数据
            noHotUrl = "https://pacaio.match.qq.com/irs/rcd?cid=146&token=49cbb2154853ef1a74ff4e53723372ce&ext=ent&page=" + page;

            System.out.println(page);
            page++;
        }
    }
}
