package com.jing.solr;

import com.jing.pojo.News;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:IndexWriterTest
 * @Description: TODO
 * @Author:RanMoAnRan
 * @Date:2019/6/8 18:22
 * @Version: 1.0
 */
@SuppressWarnings("all")
public class IndexWriterTest {
    private static final String baseURL = "http://localhost:8080/solr/collection1";

    public static void main(String[] args) throws IOException, SolrServerException {
        List<SolrInputDocument> documentList = new ArrayList<SolrInputDocument>();
        //创建solr服务器的链接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseURL);
        // 创建document对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "1136904208696610816");
        document.addField("title", "林志玲半年前曾秀恩爱 穿婚纱发文：轮到我了吗");
        document.addField("content", "林志玲秀恩爱 网易娱乐6月6日报道 6月6日，林志玲宣布与日本男子组合EXILE成员Akira结婚。Akira在微博透露二人于2018年年底开始交往。有网友发现，2018年12月13日，林志玲曾在社交软件晒出身穿婚纱的照片，配文称“轮到我了吗”，与官宣恋爱时间基本重合");
        document.addField("docurl", "https://ent.163.com/19/0606/21/EH133OGG00038FO9.htm");
        document.addField("click", "100000");
        documentList.add(document);

        SolrInputDocument document1 = new SolrInputDocument();
        document1.addField("id", "1136248568986861568");
        document1.addField("title", "辣妈颖儿身材纤细索吻小月亮，付辛博怀抱女儿爱意满满");
        document1.addField("content", "6月5日，颖儿付辛博带着女儿“小月亮”现身机场，一家三口其乐融融，画面非常温馨。只见颖儿休闲打扮现身，身穿白T恤外搭牛仔外套和高腰牛仔短裤，尽显“辣妈”身材，不得不说瘦下来之后的颖儿更加自信，穿衣风……");
        document1.addField("docurl", "https://ent.163.com/19/0606/21/EH133OGG00038FO9.htm");
        document1.addField("click", "15200");
        documentList.add(document1);

        SolrInputDocument document2 = new SolrInputDocument();
        document2.addField("id", "1136248569028804608");
        document2.addField("title", "年纳税12亿，微商夫妇真这么“大赚特赚”？");
        document2.addField("content", "（资料图/图）全文共5044字，阅读大约需要14分钟拉到更多的人，不断开新的卡，在TST的业务模型中至关重要。自从六年前达尔威成立以来，上市噱头便从未间断，在不同时期以不同的版本问世。几乎每一个TS……");
        document2.addField("docurl", "https://ent.163.com/19/0606/21/EH133OGG00038FO9.htm");
        document2.addField("click", "50000");
        documentList.add(document2);

        SolrInputDocument document3 = new SolrInputDocument();
        document3.addField("id", "1136248569049776128");
        document3.addField("title", "陈羽凡酒后车内猛吸雪茄，状态迷醉憔悴，两天前还卖阳光父亲人设");
        document3.addField("content", "去年，陈羽凡吸毒被抓一事在网络上引起轩然大波，12月4日，根据“社区戒毒决定书”显示，陈羽凡要接受为期三年的社区戒毒，虽然之后没有人见到他去所在社区报到，但此后的陈羽凡也是变得极为低调。近日，有媒体……");
        document3.addField("docurl", "https://ent.163.com/19/0606/21/EH133OGG00038FO9.htm");
        document3.addField("click", "2000");
        documentList.add(document3);

        SolrInputDocument document4 = new SolrInputDocument();
        document4.addField("id", "1136904208696610816");
        document4.addField("title", "林志玲半年前曾秀恩爱 穿婚纱发文：轮到我了吗");
        document4.addField("content", "林志玲秀恩爱 网易娱乐6月6日报道 6月6日，林志玲宣布与日本男子组合EXILE成员Akira结婚。Akira在微博透露二人于2018年年底开始交往。有网友发现，2018年12月13日，林志玲曾在社交软件晒出身穿婚纱的照片，配文称“轮到我了吗”，与官宣恋爱时间基本重合");
        document4.addField("docurl", "https://ent.163.com/19/0606/21/EH133OGG00038FO9.htm");
        document4.addField("click", "9000");
        documentList.add(document4);


        //发送post请求,添加文档
        httpSolrServer.add(documentList);
        //提交
        httpSolrServer.commit();
        //释放资源
        httpSolrServer.shutdown();
    }


    /**
     * 添加文档: 使用javabean
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void indexWriterJavaBeanTest() throws IOException, SolrServerException {
        //创建solr服务器的链接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseURL);
        News news = new News();
        news.setId("1136904225100533760");
        news.setTitle("苍井优曾多次被曝与男星相恋 山里亮太表示不担心");
        news.setContent("苍井优和山里亮太结婚见面会 网易娱乐6月6日报道 据日本媒体报道，6月5日，女星苍井优和谐星山里亮太在东京举行结婚见面会，宣布已于6月3日注册结婚，比起苍井优山里亮太名气不高，而且苍井优曾多次被曝光与合作男星相恋，但山里亮太表示不担心她的“魔性”。  苍井优一直被成为“桃花女”，2010年与合作NHK大河剧《龙马传》的大森南朋恋情曝光，2011年与铃木浩介合作舞台剧《妹妹》，第二年12月就被曝光恋情，2013年分手，2012年合作舞台剧《ZIPANG　PUNK》，2013年合作电影《宇宙海賊》的三浦春马也在2013年传过绯闻，2016年拍摄电影《安昙春子下落不明》之后又与合作演员石崎修一交往。 苍井优可以说是演艺圈最有魅力的女星，演艺之路也一直顺风顺水，1999年凭借音乐剧出道，2004年就主演了电影《花与爱丽丝》。但是对于感情方面山里亮太并不担心，他说：“确实有太多人喜欢苍井优，我只觉得能闪婚太好了，确实好多人担心我们，但是真的不用担心，因为我了解大家所不知道的苍井优，真的好像吃到最美味的食物喜极而泣一样，可能跟大家想的不太一样吧。虽然用了魔性这个词，实际上完全不是这么回事，关于这一点产生的一切联想都不必担心。” 结婚见面会上主持人还问是否担心对方出轨，山里亮太和苍井优给出同样的答案，就是不担心出轨问题。苍井优最后还说：“亮太对于工作的态度值得我尊敬，希望能跟他共度余生。我属于比较懈怠的类型，有工作了就做一下，看到亮太我觉得这样不对，他给了我勇气。今后我不仅要自己努力，还要尽全力支持亮太");
        news.setDocurl("https://ent.163.com/19/0606/16/EH0I6OSE00038FO9.html");
        news.setClick(23000045);
        httpSolrServer.addBean(news);
        httpSolrServer.commit();
        httpSolrServer.shutdown();
    }

    /**
     * 索引库修改操作(先删除,再添加) id相同即修改，不同则为添加
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void updateIndexTest() throws IOException, SolrServerException {
        //创建solr服务器的链接对象
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseURL);
        News news = new News();
        news.setId("1136904225100533760");
        news.setTitle("苍井优曾多次被曝与男星相恋 山里亮太表示不担心2");
        news.setContent("苍井优和山里亮太结婚见面会 网易娱乐6月6日报道 据日本媒体报道，6月5日，女星苍井优和谐星山里亮太在东京举行结婚见面会，宣布已于6月3日注册结婚，比起苍井优山里亮太名气不高，而且苍井优曾多次被曝光与合作男星相恋，但山里亮太表示不担心她的“魔性”。  苍井优一直被成为“桃花女”，2010年与合作NHK大河剧《龙马传》的大森南朋恋情曝光，2011年与铃木浩介合作舞台剧《妹妹》，第二年12月就被曝光恋情，2013年分手，2012年合作舞台剧《ZIPANG　PUNK》，2013年合作电影《宇宙海賊》的三浦春马也在2013年传过绯闻，2016年拍摄电影《安昙春子下落不明》之后又与合作演员石崎修一交往。 苍井优可以说是演艺圈最有魅力的女星，演艺之路也一直顺风顺水，1999年凭借音乐剧出道，2004年就主演了电影《花与爱丽丝》。但是对于感情方面山里亮太并不担心，他说：“确实有太多人喜欢苍井优，我只觉得能闪婚太好了，确实好多人担心我们，但是真的不用担心，因为我了解大家所不知道的苍井优，真的好像吃到最美味的食物喜极而泣一样，可能跟大家想的不太一样吧。虽然用了魔性这个词，实际上完全不是这么回事，关于这一点产生的一切联想都不必担心。” 结婚见面会上主持人还问是否担心对方出轨，山里亮太和苍井优给出同样的答案，就是不担心出轨问题。苍井优最后还说：“亮太对于工作的态度值得我尊敬，希望能跟他共度余生。我属于比较懈怠的类型，有工作了就做一下，看到亮太我觉得这样不对，他给了我勇气。今后我不仅要自己努力，还要尽全力支持亮太");
        news.setDocurl("https://ent.163.com/19/0606/16/EH0I6OSE00038FO9.html");
        news.setClick(23000045);
        httpSolrServer.addBean(news);
        httpSolrServer.commit();
        httpSolrServer.shutdown();
    }

    /**
     * 删除操作
     *
     * @throws IOException
     * @throws SolrServerException
     */
    @Test
    public void indexDeleteTest() throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = new HttpSolrServer(baseURL);

        //根据id删除
        //httpSolrServer.deleteById("10");
        //根据查询删除
        //httpSolrServer.deleteByQuery("title:change.me");
        //删除所有数据
        //httpSolrServer.deleteByQuery("*:*");
        httpSolrServer.commit();
        httpSolrServer.shutdown();
    }
}
