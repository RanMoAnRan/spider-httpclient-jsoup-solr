package com.jing.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:HttpClientUtilsTest
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/6/2 10:45
 * @Version 1.0
 */
@SuppressWarnings("all")
public class HttpClientUtilsTest {
    @Test
    public void doGetTest() throws IOException {
        //String url = "https://www.mzitu.com/";
        String url = "https://www.mzitu.com/page/4/";


        String html = HttpClientUtils.doGet(url);
        Document document = Jsoup.parse(html);
        //System.out.println(document);

        Elements imgs = document.select("#pins > li:nth-child(1) > a > img");
        for (Element img : imgs) {
            String src = img.attr("data-original");
            String title = img.attr("alt");
            System.out.println(title + "-----------" + src);

            //保存图片到本地
            downloadPicture(src, title);
        }

        //下 一页url
        Elements nextas = document.select("body > div.main > div.main-content > div.postlist > nav > div > a.next.page-numbers");
        String nexturl = nextas.attr("href");
        System.out.println(nexturl);
            /*if (nexturl == null || nexturl.equals(null)) {
                break;
            } else {*/
        url = nexturl;
        /* }*/


    }

    @Test
    public void doPsotTest() throws IOException {
        String url = "http://www.17see.net/";
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "zhangsan");
        map.put("age", "18");
        String html = HttpClientUtils.doPost(url, map);
        Document document = Jsoup.parse(html);
        System.out.println(document);
    }

    private static void downloadPicture(String urlList, String title) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);


            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0");

            urlConnection.setRequestProperty("Refere","https://www.mzitu.com/185441");

            DataInputStream inputStream = new DataInputStream(urlConnection.getInputStream());
            //InputStream inputStream = urlConnection.getInputStream();

            String imageName = "F:/meizi2/" + title + ".jpg";

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
            System.out.println("下载成功++++++++++++++" + title);
            System.out.println();

            inputStream.close();
            fileOutputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void doTest() throws IOException {
        String url = "https://i.meizitu.net/2019/05/18b01.jpg";
        String html = HttpClientUtils.doGet(url);

        Document document = Jsoup.parse(html);

    }
}
