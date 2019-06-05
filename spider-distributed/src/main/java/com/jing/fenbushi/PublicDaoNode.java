package com.jing.fenbushi;

import com.google.gson.Gson;
import com.jing.dao.NewsDao;
import com.jing.pojo.News;
import com.jing.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @ClassName:PublicDaoNode
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/6/5 18:18
 * @Version 1.0
 */
public class PublicDaoNode {
    private static NewsDao newsDao = new NewsDao();

    public static void main(String[] args) {
        Gson gson = new Gson();

        Jedis jedis = JedisUtils.getJedis();

        while (true) {
            List<String> list = jedis.brpop(10, "bigData:spider:newsJson");
            if (list == null || list.size() == 0) {
                break;
            }
            String newsjson = list.get(1);
            News news = gson.fromJson(newsjson, News.class);

            System.out.println(news);
            //3) 去重判断

            Boolean flag = jedis.sismember("bigData:spider:docurl", news.getDocurl());
            if (flag) {
                // 跳过本次循环
                continue;
            }

            //保存数据到数据库
            newsDao.saveBeans(news);

            //将保存完之后将url地址存入redis中
            jedis.sadd("bigData:spider:docurl", news.getDocurl());

        }
        jedis.close();
    }
}
