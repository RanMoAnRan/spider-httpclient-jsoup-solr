package com.jing.solr;

import com.jing.pojo.News;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.util.List;

/**
 * @ClassName:IndexSearchTest
 * @Description: 索引搜索相关的的方法
 * @Author:RanMoAnRan
 * @Date:2019/6/8 21:08
 * @Version: 1.0
 */
public class IndexSearchTest {
    public static final String baseUrl = "http://localhost:8080/solr/collection1";

    /**
     * 索引库的搜索(最基本的查询)
     * @throws SolrServerException
     */
    @Test
    public void indexSearchTest01() throws SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseUrl);
        SolrQuery query = new SolrQuery("title:苍井");

        //设置rows
        query.setRows(Integer.MAX_VALUE);

        QueryResponse response = httpSolrServer.query(query);

        //返回文档的集合
        SolrDocumentList documents = response.getResults();
        for (SolrDocument document : documents) {
            System.out.println("id:" + document.get("id"));
            System.out.println("title: " + document.get("title"));
            System.out.println("content: " + document.get("content"));
            System.out.println("docurl: " + document.get("docurl"));
            System.out.println("click:" + document.get("click"));
        }

        //返回bean对象
        List<News> newsList = response.getBeans(News.class);
        for (News news : newsList) {
            System.out.println(news);
        }

        httpSolrServer.shutdown();
    }


    //高级查询
    @Test
    public void indexSearchHighTest() throws SolrServerException {
        HttpSolrServer httpSolrServer=new HttpSolrServer(baseUrl);

        //匹配查询
        //SolrQuery solrQuery = new SolrQuery("title:陈羽凡");

        //通配符查询
        //SolrQuery solrQuery = new SolrQuery("title:陈*");

        //模糊匹配
        //SolrQuery solrQuery = new SolrQuery("title:陈~2");

        //区间查询 是否包含边界值  [包含] {不包含}
        //SolrQuery solrQuery = new SolrQuery("click:[10000 TO *]");

        //组合查询: AND 与  OR 或   NOT  非
        //SolrQuery solrQuery = new SolrQuery("title:陈* AND  click:[1000 TO *] OR title:苍井");

        SolrQuery solrQuery = new SolrQuery("(click:[10000 TO *]) AND (title:苍井)");



        QueryResponse response = httpSolrServer.query(solrQuery);

        List<News> newsList = response.getBeans(News.class);
        for (News news : newsList) {
            System.out.println(news);
        }
        httpSolrServer.shutdown();
    }



}
