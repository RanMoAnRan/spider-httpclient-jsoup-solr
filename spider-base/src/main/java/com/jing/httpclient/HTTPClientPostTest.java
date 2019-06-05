package com.jing.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @ClassName:HTTPClientPostTest
 * @Description TODO 使用httpClient发送post请求
 * @author:RanMoAnRan
 * @Date:2019/5/31 20:53
 * @Version 1.0
 */
@SuppressWarnings("all")
public class HTTPClientPostTest {
    public static void main(String[] args) throws IOException {
        // 确定首页url
        String indexUrl = "http://www.itcast.cn";

        //创建 httpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建请求方式的对象 : get  和 post
        HttpPost httpPost = new HttpPost(indexUrl);

        //设置请求参数
        ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("username", "jingge"));
        list.add(new BasicNameValuePair("password", "123456"));
        HttpEntity formEntity = new UrlEncodedFormEntity(list);

        httpPost.setEntity(formEntity);

        httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");

        //发送请求, 返回响应对象response
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //从响应对象获取数据:
        // 响应行:状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            //响应头
            Header[] headers = response.getHeaders("Ali-Swift-Global-Savetime");
            for (Header header : headers) {
                System.out.println(header.getValue());
            }
            //响应体
            HttpEntity entity = response.getEntity();
            // InputStream in = entity.getContent(); 如果获取的数据是一个图片或者视频字节类型的数据
            String html = EntityUtils.toString(entity);
            // System.out.println(html);

            //解析数据
            Document document = Jsoup.parse(html);
            Elements aEls = document.select(".nav_txt>ul>li>a");
            for (Element aEl : aEls) {
                System.out.println(aEl.text());
            }

        }

        //释放资源
        httpClient.close();
    }
}
