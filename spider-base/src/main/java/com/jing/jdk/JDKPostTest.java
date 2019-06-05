package com.jing.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName:JDKPostTest
 * @Description TODO   使用原生jdk发送post请求
 * @author:RanMoAnRan
 * @Date:2019/5/31 20:02
 * @Version 1.0
 */
@SuppressWarnings("all")
public class JDKPostTest {
    public static void main(String[] args) throws IOException {
        //确定首页url:
        String indexUrl = "http://www.itcast.cn";
        //将这个字符串的url 转换成 url对象
        URL url = new URL(indexUrl);
        //通过url对象, 打开一个远程的连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //设置相关的参数: 请求方式, 请求参数, 请求头....
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);//打开输出流, 支持输出内容

        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write("username=laoz&password=12321321".getBytes());//设置参数，此网站不需要参数，模拟传参

        //通过流的获取数据 io流(本地流, 网络流)
        InputStream inputStream = urlConnection.getInputStream();

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        //释放资源
        //outputStream.close();
        inputStream.close();
    }
}
