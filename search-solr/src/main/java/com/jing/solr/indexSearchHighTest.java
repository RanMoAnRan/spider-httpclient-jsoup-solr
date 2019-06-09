package com.jing.solr;

import com.jing.pojo.News;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:indexSearchHighTest
 * @Description: 高级查询
 * @Author:RanMoAnRan
 * @Date:2019/6/8 21:24
 * @Version: 1.0
 */
public class indexSearchHighTest {
    private static final String basrURL = "http://localhost:8080/solr/collection1";

    //分页
    @Test
    public void pageSearchTest() throws SolrServerException {
        Integer page = 2;
        Integer pagesize = 2;

        HttpSolrServer httpSolrServer=new HttpSolrServer(basrURL);

        SolrQuery solrQuery=new SolrQuery("*:*");

        //设置分页
        Integer start=(page-1)*pagesize;
        solrQuery.setStart(start);
        solrQuery.setRows(pagesize);

        //设置排序，倒叙
        solrQuery.setSort("click",SolrQuery.ORDER.desc);

        QueryResponse response = httpSolrServer.query(solrQuery);


        List<News> newsList = response.getBeans(News.class);
        for (News news : newsList) {
            System.out.println(news);
        }
    }

    //高亮设置
    @Test
    public void pageSearchHighLightingTest() throws SolrServerException {
       HttpSolrServer httpSolrServer=new HttpSolrServer(basrURL);
        SolrQuery solrQuery = new SolrQuery("title:苍井");
        //开启高亮
        solrQuery.setHighlight(true);
        //设置高亮的字段
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");
        //高亮的前缀和后缀
        solrQuery.setHighlightSimplePre("<em style='color:red'>");
        solrQuery.setHighlightSimplePost("</em>");


        QueryResponse response = httpSolrServer.query(solrQuery);
        List<News> newsList = response.getBeans(News.class);

        //获取高亮的内容
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for (News news : newsList) {
            String newsId = news.getId();
            //根据文档的id获取当前文档的高亮内容
            Map<String, List<String>> stringListMap = highlighting.get(newsId);

            //根据高亮的字段名称,获取当前字段的高亮内容
            List<String> titleHigh = stringListMap.get("title");

            //判断当前是否有高亮内容
            if (highlighting!=null&&highlighting.size()>0) {
                String liangtitle = titleHigh.get(0);

                news.setTitle(liangtitle);
            }

            List<String> highcontent = stringListMap.get("content");
            if (highcontent!=null&&highcontent.size()>0) {
                String liangcontent = highcontent.get(0);
                news.setContent(liangcontent);
            }


            System.out.println(newsList);
        }

        httpSolrServer.shutdown();

    }
}
