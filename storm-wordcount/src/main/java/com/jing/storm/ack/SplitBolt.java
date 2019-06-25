package com.jing.storm.ack;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;
import java.util.Random;

/**
 * @author RanMoAnRan
 * @ClassName: SplitBolt
 * @projectName spider-httpclient-jsoup
 * @description: 处理成功后,会自动调用ack方法,处理失败,爬出一个FailedException
 * @date 2019/6/23 16:52
 */
public class SplitBolt extends BaseBasicBolt {
    private Random random;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        this.random = new Random();
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            //获取上游过来的数据
            String sentence = input.getStringByField("sentence");

            //处理数据(业务逻辑)

            //随机报错
            int num = random.nextInt(5);
            num  = 1 / num;

        } catch (Exception e) {
            //处理失败, 抛出异常
            throw  new FailedException(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
