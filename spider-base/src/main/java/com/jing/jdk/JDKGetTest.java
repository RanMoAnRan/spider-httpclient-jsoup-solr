package com.jing.jdk;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @ClassName:JDKGetTest
 * @Description TODO 使用原生jdk实现get请求
 * @author:RanMoAnRan
 * @Date:2019/5/31 19:49
 * @Version 1.0
 */
public class JDKGetTest {
    public static void main(String[] args) throws IOException {
        //确定url
        String indexUrl = "https://www.mzitu.com/";
        //将这个字符串的url 转换成 url对象
        URL url = new URL(indexUrl);
        //通过url对象, 打开一个远程的连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置相关的参数: 请求方式, 请求参数, 请求头....
        urlConnection.setRequestMethod("GET");//默认就是GET
        //通过流的获取数据 io流(本地流, 网络流)
        InputStream inputStream = urlConnection.getInputStream();

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }

        //释放资源
        inputStream.close();
    }
}
