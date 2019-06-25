package com.jing.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: ReadFileSpout
 * @projectName spider-httpclient-jsoup
 * @description: 读取文件中的数据
 * @date 2019/6/20 17:51
 */
public class ReadFileSpout extends BaseRichSpout {
    private BufferedReader bufferedReader;
    private SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            bufferedReader = new BufferedReader(new FileReader("G:\\test.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        try {

            String line = bufferedReader.readLine();
            if (line != null && line.length() > 0) {
                //向下游发送句子
                collector.emit(new Values(line));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }
}
