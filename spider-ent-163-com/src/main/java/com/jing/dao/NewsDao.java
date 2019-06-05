package com.jing.dao;

import com.jing.pojo.News;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * @ClassName:NewsDao
 * @Description TODO
 * @author:RanMoAnRan
 * @Date:2019/6/2 20:50
 * @Version 1.0
 */
public class NewsDao extends JdbcTemplate {
    public NewsDao() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        super.setDataSource(dataSource);
    }


    public void saveBeans(News news) {
        String[] params = {news.getId(), news.getTitle(), news.getTime(),
                news.getSource(), news.getContent(), news.getEditor(), news.getDocurl()};
        update("insert into news  values (?,?,?,?,?,?,?)", params);
    }

}
