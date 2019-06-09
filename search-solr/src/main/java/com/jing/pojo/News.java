package com.jing.pojo;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @ClassName:News
 * @Description: TODO
 * @Author:RanMoAnRan
 * @Date:2019/6/8 20:45
 * @Version: 1.0
 */
public class News {
    //就是将这个id属性的内容映射到solr的id字段上
    @Field(value = "id")
    private String id;

    //属性名和solr的字段名称一致:value属性不需要写  不一致需要添加value属性(里面写的是solr字段的名称)
    @Field
    private String title;

    @Field
    private String content;

    @Field
    private long click;

    @Field
    private String docurl;


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

    public long getClick() {
        return click;
    }

    public void setClick(long click) {
        this.click = click;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }


    @Override
    public String toString() {
        return "News{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", click=" + click +
                ", docurl='" + docurl + '\'' +
                '}';
    }
}
