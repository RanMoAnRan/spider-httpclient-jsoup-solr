package com.jing.anli;

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

import java.io.IOException;

/**
 * @ClassName:QiDianSpider
 * @Description TODO 爬取起点中文网的一本小说
 * @author:RanMoAnRan
 * @Date:2019/5/31 21:12
 * @Version 1.0
 */
public class QiDianSpider {
    public static void main(String[] args) throws IOException {
        //确定首页url
        String indexUrl = "https://read.qidian.com/chapter/kq91DEOge6h51l-ULjeaVw2/7CJ9iUZBFmj4p8iEw--PPw2";

        while (true) {
            //创建HTTPClient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //创建请求方式
            HttpGet httpGet = new HttpGet(indexUrl);
            // 设置请求参数和请求头
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0");
            //发送请求, 获取响应对象
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                String html = EntityUtils.toString(entity, "utf-8");
                //System.out.println(html);

                //解析数据，转换成document对象
                Document document = Jsoup.parse(html);

                //获取章节名称
                Elements name = document.select(".j_chapterName");
                System.out.println(name.text());

                //获取章节内容
                Elements zjnr = document.select("div[class=read-content j_readContent] p");
                for (Element element : zjnr) {
                    System.out.println(element.text());
                }

                //获取下一章URL
                Elements aEls = document.select("#j_chapterNext");//得到a标签对象
                String href = aEls.attr("href");
                //判断是否已经爬完所有免费章节
                if (href.contains("read.qidian.com")) {
                    indexUrl = "Https:" + href;
                    System.out.println(indexUrl);
                } else {
                    break;
                }

            }
            //释放资源
            httpClient.close();
        }
    }
}
