package com.jing.storm;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: WordCountBolt
 * @projectName spider-httpclient-jsoup
 * @description: 单词统计的Bolt
 * @date 2019/6/20 18:06
 */
public class WordCountBolt extends BaseBasicBolt {
    private Map<String, Integer> map;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        this.map = new HashMap<>();
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String word = input.getStringByField("word");
        Integer count = input.getIntegerByField("count");

        if (map.get(word) != null) {
            map.put(word, map.get(word) + count);
        } else {
            map.put(word, count);
        }
        System.out.println(map);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
