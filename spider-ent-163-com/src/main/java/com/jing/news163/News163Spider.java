package com.jing.news163;

import com.google.gson.Gson;
import com.jing.dao.NewsDao;
import com.jing.pojo.News;
import com.jing.utils.HttpClientUtils;
import com.jing.utils.IdWorker;
import com.jing.utils.JedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:News163Spider
 * @Description TODO  爬取https://ent.163.com/的娱乐新闻数据
 * @author:RanMoAnRan
 * @Date:2019/6/2 19:34
 * @Version 1.0
 */
public class News163Spider {
    //防止创建多个连接池
    private static NewsDao newsDao = new NewsDao();
    //创建idworker对象
    private static IdWorker idWorker = new IdWorker(0, 0);


    public static void main(String[] args) throws IOException {

        //存放不同类型的url集合
        List<String> urls = new ArrayList<String>();
        urls.add("https://ent.163.com/special/000380VU/newsdata_index.js?callback=data_callback");
        urls.add("https://ent.163.com/special/000380VU/newsdata_star.js?callback=data_callback");
        urls.add("https://ent.163.com/special/000380VU/newsdata_movie.js?callback=data_callback");
        urls.add("https://ent.163.com/special/000380VU/newsdata_tv.js?callback=data_callback");
        urls.add("https://ent.163.com/special/000380VU/newsdata_show.js?callback=data_callback");
        urls.add("https://ent.163.com/special/000380VU/newsdata_music.js?callback=data_callback");
        for (int i = 0; i < urls.size(); i++) {
            String indexUrl = urls.get(i);
            page(indexUrl);
        }
        /*String jsonSrt = HttpClientUtils.doGet(indexUrl);
        jsonSrt = paseJson(jsonSrt);
        parseNewsJson(jsonSrt);*/
    }

    //处理json数据方法: 将非标准格式的json转换为标准json
    public static String paseJson(String jsonStr) {
        return jsonStr.substring(jsonStr.indexOf("(") + 1, jsonStr.lastIndexOf(")"));
    }

    //解析新闻列表页的数据方法
    public static void parseNewsJson(String jsonStr) throws IOException {
        //创建 gson对象
        Gson gson = new Gson();
        List<Map<String, Object>> newslist = gson.fromJson(jsonStr, List.class);
        for (Map<String, Object> newsmap : newslist) {
            // 获取新闻:  id 新闻标题, 内容, 时间, 编辑(作者), 来源, 新闻的url,
            // 分析发现, 在map中没有所要的全部数据, 所有的数据都在详情页中, 需要重新发送请求, 获取详情页的数据
            String docurl = (String) newsmap.get("docurl");

            if (docurl.contains("photoview")) {
                continue;
            }
            if (docurl.contains("v.163.com")) {
                continue;
            }
            if (docurl.contains("dy.163.com")) {
                continue;
            }

            //使用redis的set去重功能去掉重复的url
            Jedis jedis = JedisUtils.getJedis();
            Boolean flag = jedis.sismember("news163", docurl);//如果存在返回true
            jedis.close();
            if (flag) {
                continue;
            }

            //解析详情页的url
            News news = parseItemNews(docurl);

            //保存数据到数据库
            newsDao.saveBeans(news);


            //将保存完之后将url地址存入redis中
            jedis = JedisUtils.getJedis();
            jedis.sadd("news163", news.getDocurl());
            jedis.close();
        }
    }


    // 解析新闻的详情页的数据
    public static News parseItemNews(String docurl) throws IOException {
        System.out.println(docurl);
        String html = HttpClientUtils.doGet(docurl);
        //System.out.println(html);
        Document document = Jsoup.parse(html);
        //解析详情页的数据
        News news = new News();
        //新闻的id:

        long id = idWorker.nextId();
        news.setId(id + "");

        //新闻的标题
        Elements h1el = document.select("#epContentLeft>h1");
        String title = h1el.text();
        // System.out.println(title);
        news.setTitle(title);

        //新闻的时间
        Elements divEl = document.select(".post_time_source");
        String timeAndSource = divEl.text();
        String[] split = timeAndSource.split("　");
        news.setTime(split[0]);

        //3.3.2.4: 新闻的来源:
        Elements sourceEl = document.select("#ne_article_source");
        news.setSource(sourceEl.text());

        //3.3.2.5: 新闻的正文:
        Elements pEls = document.select("#endText p");
        String content = pEls.text();
        news.setContent(content);

        //3.3.2.6: 新闻的编辑:
        Elements editorEl = document.select(".ep-editor");
        String editor = editorEl.text();
        editor = editor.substring(editor.indexOf("：") + 1, editor.lastIndexOf("_"));
        news.setEditor(editor);

        //3.3.2.7: 新闻的url路径:
        news.setDocurl(docurl);

        System.out.println(news);
        return news;
    }

    //分页获取数据
    public static void page(String indexUrl) throws IOException {
        String page = "02";

        //发送请求
        while (true) {
            String jsonStr = HttpClientUtils.doGet(indexUrl);
            if (jsonStr == null) {
                System.out.println("数据获取完成....");
                break;
            }
            jsonStr = paseJson(jsonStr);
            //解析数据
            parseNewsJson(jsonStr);


            //获取下一页的url
            if (indexUrl.contains("newsdata_index")) {
                indexUrl = "https://ent.163.com/special/000380VU/newsdata_index_" + page + ".js?callback=data_callback";
            }
            if (indexUrl.contains("newsdata_star")) {
                indexUrl = "https://ent.163.com/special/000380VU/newsdata_star_" + page + ".js?callback=data_callback";
            }
            if (indexUrl.contains("newsdata_movie")) {
                indexUrl = "https://ent.163.com/special/000380VU/newsdata_movie_" + page + ".js?callback=data_callback";
            }
            if (indexUrl.contains("newsdata_tv")) {
                indexUrl = "https://ent.163.com/special/000380VU/newsdata_tv_" + page + ".js?callback=data_callback";
            }
            if (indexUrl.contains("newsdata_show")) {
                indexUrl = "https://ent.163.com/special/000380VU/newsdata_show_" + page + ".js?callback=data_callback";
            }
            if (indexUrl.contains("newsdata_music")) {
                indexUrl = "https://ent.163.com/special/000380VU/newsdata_music_" + page + ".js?callback=data_callback";
            }

            int i = Integer.parseInt(page);
            i++;
            if (i >= 10) {
                page = i + "";
            } else {
                page = "0" + i;
            }
        }
    }

}
