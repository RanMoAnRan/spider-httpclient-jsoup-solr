package com.jing.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @author RanMoAnRan
 * @ClassName: WordCountTopology
 * @projectName spider-httpclient-jsoup
 * @description: TopologyMain 驱动类 组装spout和bolt
 * @date 2019/6/20 18:16
 */
public class WordCountTopology {
    public static void main(String[] args) {
        //创建拓扑任务
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("readFileSpout", new ReadFileSpout());
        topologyBuilder.setBolt("splitBolt", new SplitBolt()).localOrShuffleGrouping("readFileSpout");
        topologyBuilder.setBolt("wordCountBolt", new WordCountBolt()).localOrShuffleGrouping("splitBolt");


        //上传的方式有两种 1本地上传 2集群上传
        LocalCluster localCluster = new LocalCluster();
        StormTopology stormTopology = topologyBuilder.createTopology();
        Config config = new Config();

        //上传任务的名称不能重复
        localCluster.submitTopology("wordCount", config, stormTopology);

    }
}
