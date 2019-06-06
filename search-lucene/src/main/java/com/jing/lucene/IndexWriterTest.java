package com.jing.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:IndexWriterTest
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/6/6 15:19
 * @Version 1.0
 */
public class IndexWriterTest {
    public static void main(String[] args) throws IOException {
        //创建索引写入器对象
        FSDirectory directory = FSDirectory.open(new File("D:\\index"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory,config);

        List<Document> docs = new ArrayList<Document>();

        //添加原始文档数据
        Document document = new Document();
        document.add(new LongField("pid",1, Field.Store.YES));
        document.add(new StringField("title","小米大买了",Field.Store.YES));
        document.add(new TextField("content","小米2017年在香港上市了，大获全胜",Field.Store.YES));
        docs.add(document);

        Document doc1  = new Document();
        doc1.add(new LongField("pid", 2, Field.Store.YES));
        doc1.add(new StringField("title", "华为P20 原价售出了", Field.Store.NO));
        doc1.add(new TextField("content", "华为手机是比较贵的, 因为华为要挣钱, 搞研发", Field.Store.YES));
        docs.add(doc1);

        Document doc2  = new Document();
        doc2.add(new LongField("pid", 3, Field.Store.YES));
        doc2.add(new StringField("title", "小米推出了小米笔记本", Field.Store.NO));
        doc2.add(new TextField("content", "小米的笔记本是性价比比较高的一款笔记本电脑", Field.Store.YES));
        docs.add(doc2);

        Document doc3  = new Document();
        doc3.add(new LongField("pid", 4, Field.Store.YES));
        doc3.add(new StringField("title", "lucene简介", Field.Store.NO));
        doc3.add(new TextField("content", "lucene是一个全文检索搜索引擎工具包, 使用lucene来构建一个全文搜索的引擎", Field.Store.YES));
        docs.add(doc3);

        //indexWriter.addDocument(document);
        indexWriter.addDocuments(docs);

        //提交
        indexWriter.commit();

        //释放资源
        indexWriter.close();
    }
}
