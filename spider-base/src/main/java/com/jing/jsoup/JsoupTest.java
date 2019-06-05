package com.jing.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @ClassName:JsoupTest
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/5/31 21:09
 * @Version 1.0
 */
public class JsoupTest {
    public static void main(String[] args) throws IOException {
        //1. 确定首页的url
        String indexUrl = "http://www.itcast.cn";

        //2. 发送请求, 获取数据
        Document document = Jsoup.connect(indexUrl).get();//此方式获取html不好设置参数，适合做测试

        //3. 解析数据
        Elements aEls = document.select(".nav_txt>ul>li>a");
        for (Element aEl : aEls) {
            System.out.println(aEl.text());
        }
    }
}
