package com.jing.storm.ticktime;

import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: SplitBolt
 * @projectName spider-httpclient-jsoup
 * @description: 定时任务bolt
 * @date 2019/6/23 17:20
 */
public class SplitBolt extends BaseBasicBolt {
    /**
     * 告诉storm框架多长时间给splitBolt发送一个定时提醒:
     * 这个方法就是设置周期时间:  5秒storm会给splitbolt的execute方法发送一个系统的tuple数据
     * 可以判断tuple是否是系统发送的tuple,来决定是否应该输出数据
     *
     * @return
     */
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        String key = Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS;
        config.put(key, 5);
        return config;
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        //判断是否是系统发送的tuple
        if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && input.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(format.format(new Date()));
        }else {
            String nameage = input.getStringByField("nameage");
            System.out.println(nameage);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
