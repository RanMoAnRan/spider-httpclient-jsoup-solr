package com.jing.storm.update;

import org.apache.commons.lang3.StringUtils;
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
 * @ClassName: FileReaderSpout
 * @projectName spider-httpclient-jsoup
 * @description: TODO
 * @date 2019/6/22 21:09
 */
public class FileReaderSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private Random random;
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.random = new Random();
        //发射器
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        String[] sentences = new String[]{ "my storm word count", "hello my storm", "hello storm hello world","how do you do","how are you"};

        //随机产生字符串
        String line = sentences[ random.nextInt(sentences.length - 1)];

        if(StringUtils.isNotEmpty(line)){
            //2.向下游发送句子
            collector.emit(new Values(line));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }
}
