package com.jing.solrcloud;

import com.jing.pojo.News;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * @ClassName:IndexWriterTest
 * @Description: solrcloud集群，索引库写入操作
 * @Author:RanMoAnRan
 * @Date:2019/6/9 22:12
 * @Version: 1.0
 */
public class IndexWriterTest {
    private static String zkHost="node01:2181,node02:2181,node03:2181";
    public static void main(String[] args) throws IOException, SolrServerException {
        //创建solr的链接对象
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
        //设置连接的是集群的那个collection,那个索引库
        cloudSolrServer.setDefaultCollection("collection2");

        //创建document对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","1136904193697779712");
        document.addField("title","追加控罪听证会举行 R-凯利否认11项新的性侵罪名");
        document.addField("content","否认11项控罪，下一次听证会将于6月26日举行");
        document.addField("docurl","https://ent.163.com/19/0607/12/EH2NHEV100038FO9.html");
        document.addField("time",new Date());
        document.addField("editor","老虎");
        document.addField("source","吃瓜群众");

        //执行添加操作
        cloudSolrServer.add(document);


        //创建javabean对象
        News news = new News();
        news.setId("1136904209795518464");
        news.setTitle("第一美女林志颖结婚？本尊回应：是志玲姐姐啦");
        news.setContent(" 网易娱乐6月6日报道 6月6日晚，林志玲发文宣布结婚。岂料有媒体将林志玲名字错打成林志颖，林志颖无奈发文“收到祝福的消息，但不是我结婚，是志玲姐姐啦，祝福她幸福快乐！”  网友纷纷回复：“笑死。。林志颖与老公相识舞台剧”“林志颖林志玲林志炫还以为你们是一家呢”“不要蹭我志玲姐姐热度行吗？” ");
        news.setDocurl("https://ent.163.com/19/0606/21/EH15JCNQ00038FO9.html");

        news.setTime(new Date());
        news.setSource("网易娱乐");
        news.setEditor("小白");

        cloudSolrServer.addBean(news);

        cloudSolrServer.commit();

        cloudSolrServer.shutdown();
    }


    /**
     * 修改索引库操作
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void updateIndexTest() throws IOException, SolrServerException {
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
        cloudSolrServer.setDefaultCollection("collection2");
        News news = new News();
        news.setId("1136904209795518464");
        news.setTitle("山里亮太被问是否担心苍井优出轨 回答让网友大赞");
        news.setContent(" 网易娱乐6月6日报道 6月6日晚，林志玲发文宣布结婚。岂料有媒体将林志玲名字错打成林志颖，林志颖无奈发文“收到祝福的消息，但不是我结婚，是志玲姐姐啦，祝福她幸福快乐！”  网友纷纷回复：“笑死。。林志颖与老公相识舞台剧”“林志颖林志玲林志炫还以为你们是一家呢”“不要蹭我志玲姐姐热度行吗？” ");
        news.setDocurl("https://ent.163.com/19/0606/21/EH15JCNQ00038FO9111.html");
        news.setTime(new Date());
        news.setSource("腾讯");
        news.setEditor("腾讯小哥");

        cloudSolrServer.addBean(news);
        cloudSolrServer.commit();
        cloudSolrServer.shutdown();
    }


    @Test
    public void deleteIndexTest() throws IOException, SolrServerException {
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
        cloudSolrServer.setDefaultCollection("collection2");
        //根据id删除
        //cloudSolrServer.deleteById("12544654");
        //根据查询删除
        cloudSolrServer.deleteByQuery("title:腾讯");
        //删除所有
        //cloudSolrServer.deleteByQuery("*:*");

        cloudSolrServer.commit();
        cloudSolrServer.shutdown();
    }

}
