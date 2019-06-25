package com.jing.storm.ticktime;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * @author RanMoAnRan
 * @ClassName: RandomSpout
 * @projectName spider-httpclient-jsoup
 * @description: 不断随机发送数据的spout
 * @date 2019/6/23 17:14
 */
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private Random random;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        this.random = new Random();
    }

    @Override
    public void nextTuple() {
        String[] names = {"张三 20", "小明 28", "老王 30", "小黑 38"};
        int nextInt = random.nextInt(names.length - 1);
        System.out.println(nextInt);
        collector.emit(new Values(names[nextInt]));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("nameage"));
    }
}
