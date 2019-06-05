package com.jing.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @ClassName:HTTPClientGetTest
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/5/31 20:24
 * @Version 1.0
 */
@SuppressWarnings("all")
public class HTTPClientGetTest {
    public static void main(String[] args) throws IOException {
        // 确定首页url
        String indexUrl = "https://www.mzitu.com/";

        //创建 httpClient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建请求方式的对象 : get  和 post
        HttpGet httpGet = new HttpGet(indexUrl);

        //设置请求参数
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");

        //发送请求, 返回响应对象response
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //从响应对象获取数据:
        // 响应行:状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            //响应头
            Header[] headers = response.getHeaders("cache-control");
            for (Header header : headers) {
                System.out.println(header.getValue());
            }
            //响应体
            HttpEntity entity = response.getEntity();
            // InputStream in = entity.getContent(); 如果获取的数据是一个图片或者视频字节类型的数据
            String html = EntityUtils.toString(entity);
            System.out.println(html);
        }

        //释放资源
        httpClient.close();
    }
}
