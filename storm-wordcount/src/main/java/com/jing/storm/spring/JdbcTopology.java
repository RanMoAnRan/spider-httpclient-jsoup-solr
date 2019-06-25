package com.jing.storm.spring;

import com.google.common.collect.Maps;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.topology.TopologyBuilder;

import java.util.HashMap;

/**
 * @author RanMoAnRan
 * @ClassName: JdbcTopology
 * @projectName spider-httpclient-jsoup
 * @description: storm和jdbc整合的拓扑开发
 * @date 2019/6/23 17:47
 */
public class JdbcTopology {
    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("random",new RandomSpout());
        topologyBuilder.setBolt("split",new SplitBolt()).localOrShuffleGrouping("random");
        //集成jdbcInsertBolt
        //创建数据库的链接对象
        HashMap<String, Object> hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://localhost/test?characterEncoding=utf-8");
        hikariConfigMap.put("dataSource.user","root");
        hikariConfigMap.put("dataSource.password","root");

        HikariCPConnectionProvider connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);

        //数据存放的表名
        String tableName = "user";
        SimpleJdbcMapper jdbcMapper = new SimpleJdbcMapper(tableName, connectionProvider);

        //创建JdbcInsertBolt对象
        JdbcInsertBolt jdbcInsertBolt = new JdbcInsertBolt(connectionProvider, jdbcMapper)
                .withTableName(tableName)
                .withQueryTimeoutSecs(30);

        topologyBuilder.setBolt("jdbcInsertBolt",jdbcInsertBolt).localOrShuffleGrouping("split");

        LocalCluster localCluster = new LocalCluster();
        Config config = new Config();
        localCluster.submitTopology("jdbcTopology",config,topologyBuilder.createTopology());
    }
}
