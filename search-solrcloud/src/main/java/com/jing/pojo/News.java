package com.jing.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName:News
 * @Description: TODO
 * @Author:RanMoAnRan
 * @Date:2019/6/9 22:10
 * @Version: 1.0
 */
public class News {
    //新闻id
    @Field
    private String id;

    //新闻标题
    @Field
    private String title;

    //新闻内容
    @Field
    private String content;

    //新闻的url
    @Field
    private String docurl;

    //新闻的编辑
    @Field
    private String editor;

    //新闻的来源
    @Field
    private String source;

    //新闻的时间
    @Field
    private String time;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    //添加一个重载的setTime方法
    public void setTime(Date time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ss'Z'");
        this.time = format.format(time);
    }

    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", docurl='" + docurl + '\'' +
                ", editor='" + editor + '\'' +
                ", source='" + source + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
