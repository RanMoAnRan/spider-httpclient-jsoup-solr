package com.jing.storm.ticktime;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @author RanMoAnRan
 * @ClassName: TickTimeTopology
 * @projectName spider-httpclient-jsoup
 * @description: 定时器的拓扑
 * @date 2019/6/23 17:30
 */
public class TickTimeTopology {
    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();


        topologyBuilder.setSpout("random",new RandomSpout());

        topologyBuilder.setBolt("split",new SplitBolt()).localOrShuffleGrouping("random");


        Config config = new Config();
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("tickTime",config,topologyBuilder.createTopology());

    }
}
