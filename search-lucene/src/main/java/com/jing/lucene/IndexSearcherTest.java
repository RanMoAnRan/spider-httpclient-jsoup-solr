package com.jing.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName:IndexSearcherTest
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/6/6 15:46
 * @Version 1.0
 */
public class IndexSearcherTest {
    // lucene的索引查询代码
    public static void main(String[] args) throws IOException, ParseException {
        //创建索引查询对象
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("D:\\index")));
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //添加查询的条件
        // 根据查询解析器获取数据 : 使用那种分词器, 取决于写入索引使用的分词器
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        //查询的词条
        Query query = queryParser.parse("小米");

        // 执行查询
        // 参数1: query:  查询的条件
        // 参数2:  n    根据条件返回前N 个数据
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);

        //查询
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;//得分文档数组
        int totalHits = topDocs.totalHits;// 根据条件一共有多少条匹配数据
        System.out.println("总条数：" + totalHits);
        for (ScoreDoc scoreDoc : scoreDocs) {
            float score = scoreDoc.score;//文档得分信息
            int doc = scoreDoc.doc;//得到id
            Document document = indexSearcher.doc(doc);//根据id得到document

            String pid = document.get("pid");
            String title = document.get("title");
            String content = document.get("content");

            System.out.println("得分：" + score + ", pid:" + pid + ", title:" + title + ", content:" + content);
        }
    }


    //将查询方法抽取出来
    public void publicQuery(Query query) throws IOException {
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(new File("D:\\index")));
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        int totalHits = topDocs.totalHits;
        System.out.println("查询到的中条数：" + totalHits);
        for (ScoreDoc scoreDoc : scoreDocs) {
            float score = scoreDoc.score;
            int doc = scoreDoc.doc;
            Document document = indexSearcher.doc(doc);
            String pid = document.get("pid");
            String title = document.get("title");
            String content = document.get("content");
            System.out.println("得分：" + score + ", pid:" + pid + ", title:" + title + ", content:" + content);
        }
    }


    //多样化查询(特殊查询)

    @Test// 词条查询: TermQuery
    public void TermQueryTest() throws IOException {
        TermQuery query = new TermQuery(new Term("content", "华为"));
        publicQuery(query);
    }


    @Test //通配符查询: WildcardQuery
    public void WildcardQueryTest() throws IOException {
        WildcardQuery query = new WildcardQuery(new Term("content", "笔记本*"));
        publicQuery(query);
    }

    @Test // 模糊查询: FuzzyQuery
    public void FuzzyQueryTest() throws IOException {
        FuzzyQuery query = new FuzzyQuery(new Term("content", "笔记本"));
        publicQuery(query);
    }

    @Test // 数值范围查询: NumericRangeQuery
    public void NumericRangeQueryTest() throws IOException {
        NumericRangeQuery<Long> query = NumericRangeQuery.newLongRange("pid", 2L, 4L, true, true);
        publicQuery(query);
    }

    @Test // 组合查询: BooleanQuery
    public void BooleanQueryTest() throws IOException {
         /*
        BooleanQuery : 本身自己是没有任何的条件
            将其他多个条件, 组合在一起, 进行多条件查询
        MUST : 表示查询的出来结果中必须要包含这个条件中数据
        MUST_NOT : 表示查询的出来结果中必须不能包含这个条件中数据
        SHOULD :  表示查询的出来结果中如果有这个条件中数据, 显示, 如果没有,不影响其他的条件
     */
        BooleanQuery query = new BooleanQuery();
        NumericRangeQuery<Long> query1 = NumericRangeQuery.newLongRange("pid", 2L, 4L, true, true);
        query.add(query1, BooleanClause.Occur.MUST);
        TermQuery query2 = new TermQuery(new Term("content", "lucene"));
        query.add(query2, BooleanClause.Occur.MUST_NOT);
        publicQuery(query);
    }

}
