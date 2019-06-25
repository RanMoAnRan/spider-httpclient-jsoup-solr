package com.jing.storm.update;

import com.jing.storm.ReadFileSpout;
import com.jing.storm.SplitBolt;
import com.jing.storm.WordCountBolt;
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
 * @ClassName: WordCountTopology
 * @projectName spider-httpclient-jsoup
 * @description: TopologyMain 驱动类 组装spout和bolt
 * @date 2019/6/20 18:16
 */
public class WordCountTopology {
    public static void main(String[] args) {
        //创建拓扑任务
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        /**
         * 一参: spout的唯一标识
         * 二参: spout的对象
         * 三个参数: 当前这个spout同时运行几个task
         */
        topologyBuilder.setSpout("readFileSpout", new FileReaderSpout(), 2);
        topologyBuilder.setBolt("splitBolt", new SplitBolt(), 4).localOrShuffleGrouping("readFileSpout");
        topologyBuilder.setBolt("wordCountBolt", new WordCountBolt(), 8).localOrShuffleGrouping("splitBolt");


        //上传的方式有两种 1本地上传 2集群上传
        LocalCluster localCluster = new LocalCluster();
        StormTopology stormTopology = topologyBuilder.createTopology();
        Config config = new Config();

        if (args != null && args.length > 0) {

            //设置总共所有的worker中启动多少个ackerBolt进行消息的确认处理
            config.setNumAckers(2);
            //设置占用worker的数量
            config.setNumWorkers(8);
            try {
                StormSubmitter.submitTopology(args[0], config, stormTopology);
            } catch (AlreadyAliveException e) {
                e.printStackTrace();
            } catch (InvalidTopologyException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }

        } else {
            //上传任务的名称不能重复
            localCluster.submitTopology("wordCount", config, stormTopology);
        }
    }
}
