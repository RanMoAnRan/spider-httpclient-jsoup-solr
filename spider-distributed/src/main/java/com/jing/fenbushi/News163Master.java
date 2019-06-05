package com.jing.fenbushi;

import com.google.gson.Gson;
import com.jing.dao.NewsDao;
import com.jing.utils.HttpClientUtils;
import com.jing.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:News163Master
 * @Description TODO 爬取163新闻的列表的详情页url
 * @author:RanMoAnRan
 * @Date:2019/6/5 17:48
 * @Version 1.0
 */
public class News163Master {
    //防止创建多个连接池
    private static NewsDao newsDao = new NewsDao();

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
            Boolean flag = jedis.sismember("bigData:spider:docurl", docurl);//如果存在返回true
            System.out.println(docurl);
            jedis.close();
            if (flag) {
                continue;
            }
            //将url地址存入redis的list中
            jedis = JedisUtils.getJedis();
            jedis.lpush("bigData:spider:163itemUrl:docurl", docurl);
            jedis.close();
        }
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
