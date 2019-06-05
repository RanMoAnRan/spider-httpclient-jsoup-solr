package com.jing.meizi;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ClassName:Meizitu
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/5/31 22:15
 * @Version 1.0
 */
@SuppressWarnings("all")
public class Meizitu {
    public static void main(String[] args) throws IOException {
        // 确定首页url
        String indexUrl = "http://www.17see.net/";

        while (true) {
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
                // System.out.println(html);

                Document document = Jsoup.parse(html);
                Elements imgs = document.select("body > div.g-doc > div.g-bd.f-cb > div.g-mn > div.m-list-main > ul > li > div > a > img");
                for (Element img : imgs) {
                    String src = img.attr("data-original");
                    src="http://www.17see.net"+src;
                    String title = img.attr("alt");
                    System.out.println(title + "-----------" + src);

                    //保存图片到本地
                    downloadPicture(src,title);
                }


                //下 一页url
                Elements nextas = document.select("body > div.g-doc > div.g-bd.f-cb > div.g-mn > div.m-page.m-page-sr.m-page-sm > a.next.page-numbers");
                String nexturl = nextas.attr("href");
                nexturl="http://www.17see.net"+nexturl;
                System.out.println(nexturl);
                if (nexturl == null || nexturl.equals(null)) {
                    break;
                } else {
                    indexUrl = nexturl;
                }

            }

            //释放资源
            httpClient.close();
        }
    }


    //链接url下载图片方式一
    private static void downloadPicture(String urlList,String title) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0");

           // DataInputStream dataInputStream = new DataInputStream(urlConnection.getInputStream());
            InputStream inputStream = urlConnection.getInputStream();

            String imageName = "F:/meizi/"+title+".jpg";

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(output.toByteArray());

            System.out.println();
            System.out.println("下载成功++++++++++++++"+title);
            System.out.println();

            inputStream.close();
            fileOutputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //下载方式二//此方式下载如果遇到图片找不到会结束，方式一会继续
    public static void downloadPicture1(String imgurl,String title) throws IOException {

        //将这个字符串的url 转换成 url对象
        URL url = new URL(imgurl);
        //通过url对象, 打开一个远程的连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0");

        //设置相关的参数: 请求方式, 请求参数, 请求头....
        urlConnection.setRequestMethod("GET");//默认就是GET
        //通过流的获取数据 io流(本地流, 网络流)
        InputStream inputStream = urlConnection.getInputStream();

        FileOutputStream fileOutputStream = new FileOutputStream("F:\\meizi2\\"+title+".jpg");


        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
           fileOutputStream.write(bytes,0,len);
        }

        System.out.println();
        System.out.println("下载成功++++++++++++++"+title);
        System.out.println();

        //释放资源
        inputStream.close();
        fileOutputStream.close();
    }
}
