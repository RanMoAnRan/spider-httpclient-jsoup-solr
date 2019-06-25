package com.jing.storm.ack;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

/**
 * @author RanMoAnRan
 * @ClassName: AckTopology
 * @projectName spider-httpclient-jsoup
 * @description: TODO
 * @date 2019/6/23 16:54
 */
public class AckTopology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //创建拓扑构建器
        TopologyBuilder topologyBuilder = new TopologyBuilder();


        //组装拓扑
        topologyBuilder.setSpout("log",new LogSpout());

        topologyBuilder.setBolt("ack",new SplitBolt()).localOrShuffleGrouping("log");


        StormTopology topology = topologyBuilder.createTopology();

        Config config = new Config();

        //设置ackbolt的个数
        config.setNumAckers(1);


        if(args != null  && args.length >0){
            StormSubmitter.submitTopology(args[0],config,topology);
        }else {
            //本地模式
            LocalCluster localCluster = new LocalCluster();

            localCluster.submitTopology("ackTopology",config,topology);
        }
    }
}
