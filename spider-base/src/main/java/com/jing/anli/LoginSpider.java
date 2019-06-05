package com.jing.anli;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @ClassName:LoginSpider
 * @Description TODO  登录成功取得cookie信息, 直接使用登录后的cookie 完成登录即可
 * @author:RanMoAnRan
 * @Date:2019/5/31 21:49
 * @Version 1.0
 */
//登陆网站获取积分  账号：itcast  密码： www.itcast.cn
public class LoginSpider {
    public static void main(String[] args) throws IOException {
        //确定url:
        String indexUrl = "http://home.manmanbuy.com/usercenter.aspx";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(indexUrl);
        httpGet.setHeader("Cookie", "ASP.NET_SessionId=sv2z4xmzfjr5ct45jw2bj345; tanchuang1231=1; 60014_mmbuser=B1ACUQoKDjlbDQZQAFQDAFYBUlMJVwoHXVFXDgFVXFIFVVNaAAAIVg%3d%3d");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0");
        httpGet.setHeader("Referer", "http://home.manmanbuy.com/login.aspx");

        CloseableHttpResponse response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        //System.out.println(statusCode);
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, "utf-8");
            //System.out.println(html);
            Document document = Jsoup.parse(html);
            //得到积分标签对象
            Elements ems = document.select("#aspnetForm > div.udivright > div:nth-child(2) > table > tbody > tr > td:nth-child(1) > table:nth-child(2) > tbody > tr > td:nth-child(2) > div:nth-child(1) > font");

            System.out.println(ems.text());
        }

        httpClient.close();
    }
}
