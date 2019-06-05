package com.jing.fenbushi;

import com.google.gson.Gson;
import com.jing.pojo.News;
import com.jing.utils.HttpClientUtils;
import com.jing.utils.IdWorker;
import com.jing.utils.JedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName:News163Slave
 * @Description TODO 解析新闻详情页数据, 封装到news对象
 * @author:RanMoAnRan
 * @Date:2019/6/5 17:56
 * @Version 1.0
 */
public class News163Slave {
    //创建idworker对象
    private static IdWorker idWorker = new IdWorker(1, 1);

    public static void main(String[] args) throws IOException {
        // 解析新闻的详情页的数据

        Gson gson = new Gson();
        //String docurl = jedis.rpop("bigData:spider:163itemUrl:docurl");
        //设置等待时间如果20秒以内出现数据继续执行
        while (true) {
            Jedis jedis = JedisUtils.getJedis();
            List<String> list = jedis.brpop(10, "bigData:spider:163itemUrl:docurl");
            jedis.close();
            if (list == null || list.size() == 0) {
                break;
            }
            String docurl = list.get(1);
            News news = parseItemNews(docurl);
            String newsjson = gson.toJson(news);
            jedis = JedisUtils.getJedis();
            //将newsjson放置到redis中
            jedis.lpush("bigData:spider:newsJson", newsjson);
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
}
