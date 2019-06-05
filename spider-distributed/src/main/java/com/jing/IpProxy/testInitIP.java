package com.jing.IpProxy;

import com.jing.utils.JedisUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @ClassName:testInitIP
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/6/5 20:41
 * @Version 1.0
 */
public class testInitIP {
    @Test
    public void test() throws IOException {
        HttpHost proxy = new HttpHost("192.145.15.42", 8080);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
        HttpGet httpGet = new HttpGet();
        System.out.println(httpclient.execute(httpGet).getStatusLine().getStatusCode());
    }


    //将代理放到redis中，存储类型list
    public void testInitIP() throws Exception {
        Jedis conn = JedisUtils.getJedis();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File("C:\\Users\\maoxiangyi\\Desktop\\Proxies2018-06-06.txt")));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            conn.lpush("spider:ip", line);
        }
        bufferedReader.close();
        conn.close();
    }


    //重构后的httpclient
    private static String execute(HttpRequestBase request) {

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)// 设置创建连接的最长时间
                .setConnectionRequestTimeout(5000)// 设置获取连接的最长时间
                .setSocketTimeout(10 * 1000)// 设置数据传输的最长时间
                .build();
        request.setConfig(requestConfig);

        String html = null;

        // 从redis中获取代理IP
        Jedis conn = JedisUtils.getJedis();
        // 从右边弹出一个元素之后，从新放回左边
        List<String> ipkv = conn.brpop(0, "spider:ip");
        // CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpClient httpClient = getProxyHttpClient(ipkv.get(1));
        try {
            CloseableHttpResponse res = httpClient.execute(request);
            if (200 == res.getStatusLine().getStatusCode()) {
                html = EntityUtils.toString(res.getEntity(), Charset.forName("utf-8"));
                //请求成功之后，将代理IP放回去，下次继续使用
                conn.lpush("spider:ip", ipkv.get(1));
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("请求失败");
            // TODO 需要开发自动重试功能
            throw new RuntimeException(e);
        }
        return html;
    }


    private static PoolingHttpClientConnectionManager connectionManager;

    private static CloseableHttpClient getProxyHttpClient(String ipkv) {

        String[] vals = ipkv.split(":");
        System.out.println(vals);
        HttpHost proxy = new HttpHost(vals[0], Integer.parseInt(vals[1]));
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return HttpClients.custom().setConnectionManager(connectionManager).setRoutePlanner(routePlanner).build();
    }
}
