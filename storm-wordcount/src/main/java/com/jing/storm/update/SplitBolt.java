package com.jing.storm.update;

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
 * @description: 切割单词的Bolt
 * @date 2019/6/20 18:00
 */
public class SplitBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取上游的句子
        String sentence = input.getStringByField("sentence");
        //按照空格切割为单词
        String[] words = sentence.split(" ");
        for (String word : words) {
            //向下发送单词和个数
            collector.emit(new Values(word, 1));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}
