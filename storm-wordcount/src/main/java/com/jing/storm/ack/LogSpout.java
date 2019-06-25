package com.jing.storm.ack;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author RanMoAnRan
 * @ClassName: LogSpout
 * @projectName spider-httpclient-jsoup
 * @description: 发送数据的spout, 随机发送数据
 * @date 2019/6/23 16:45
 */
public class LogSpout extends BaseRichSpout{
    private Random random;

    private SpoutOutputCollector collector;

    //使用线程安全的Integer类
    private AtomicInteger msgId;

    //第一个:msgId,  第二个: 消息数据, 记录已经发送出去的消息
    private Map<String,String> pendingMsg;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.random = new Random();
        this.collector = collector;
        this.msgId = new AtomicInteger(0);
        this.pendingMsg = new ConcurrentHashMap<>();
    }

    @Override
    public void nextTuple() {
        try {
            Thread.sleep(3000);
            //定义数组，作为数据来源
            String[] sentences = new String[]{ "my storm word count", "hello my storm", "hello storm hello world","i love you","you love her","how do you do"};

            String line = sentences[random.nextInt(sentences.length - 1)];


            //下下游发送,  第二个参数: 消息的msgid   如果需要开启ack机制,必须携带msgId这个字段
            //msgId必须唯一
            int id = msgId.incrementAndGet();
            collector.emit(new Values(line), id);

            //记录发送出去的消息
            pendingMsg.put(""+id,line);

            System.out.println("msgId=" + id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }


    /**
     * 消息被消费成功后,被调用的方法
     * @param msgId
     */
    @Override
    public void ack(Object msgId) {
        //消费成功后,将成功的消息remove掉
        pendingMsg.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        System.out.println("消费失败:" + msgId);

        //消息处理失败了,根据场景而定, 扩展: 消息如何重新发送呢?
        String msg = pendingMsg.get(msgId);
        //重新发送
        collector.emit(new Values(msg), msgId);
    }
}
