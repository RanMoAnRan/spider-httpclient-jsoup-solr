package com.jing.solrcloud;

import com.jing.pojo.News;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.Date;
import java.util.List;

/**
 * @ClassName:IndexSearchTest
 * @Description: solrcloud查询
 * @Author:RanMoAnRan
 * @Date:2019/6/10 15:41
 * @Version: 1.0
 */
public class IndexSearchTest {
    private static String zkHost="192.168.72.141:2181,192.168.72.142:2181,192.168.72.143:2181";
    public static void main(String[] args) throws SolrServerException {
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
        cloudSolrServer.setDefaultCollection("collection2");

        SolrQuery solrQuery = new SolrQuery("*:*");

        //solrQuery中何以设置分页，排序，高亮设置

        QueryResponse response = cloudSolrServer.query(solrQuery);

        SolrDocumentList results = response.getResults();
        for (SolrDocument document : results) {
            String id = (String) document.get("id");
            System.out.println("id:" + id);
            String title = (String) document.get("title");
            System.out.println("title:" + title);
            String content = (String) document.get("content");
            System.out.println("content:" + content);
            String editor = (String) document.get("editor");
            System.out.println("editor:" + editor);
            String docurl = (String) document.get("docurl");
            System.out.println("docurl:" + docurl);
            String source = (String) document.get("source");
            System.out.println("source:" + source);
            Date time = (Date) document.get("time");
            System.out.println("time:" + time);
        }



       /* List<News> newsList = response.getBeans(News.class);
        for (News news : newsList) {
            System.out.println(news);
        }*/
        cloudSolrServer.shutdown();
    }
}
