package com.jing.storm.spring;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @author RanMoAnRan
 * @ClassName: SplitBolt
 * @projectName spider-httpclient-jsoup
 * @description: TODO
 * @date 2019/6/23 17:44
 */
public class SplitBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String nameage = input.getStringByField("nameage");
        String[] split = nameage.split(" ");
        collector.emit(new Values(null,split[0],Integer.parseInt(split[1])));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
//字段名称和数据库中的字段名称一致
        declarer.declare(new Fields("userId","name","age"));
    }
}
