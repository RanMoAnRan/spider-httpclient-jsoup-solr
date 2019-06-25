package com.jing.fenbushi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author RanMoAnRan
 * @ClassName: KafkaSpiderProducer
 * @projectName spider-httpclient-jsoup
 * @description: 将新闻数据写入kafka集群的封装类
 * @date 2019/6/16 17:53
 */
public class KafkaSpiderProducer {
    private static Properties props = null;

    private static Producer<String, String> producer = null;

    static {
        props = new Properties();
        //kafka集群的地址
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        //消息的确认机制
        props.put("acks", "all");
        //消息的重试
        props.put("retries", 0);
        //消息的批量大小
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        //消息的批量缓存大小
        props.put("buffer.memory", 33554432);
        //消息的key的序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //消息的value的序列化方式
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);
    }


    public static void sendNewsJson(String jsonStr) {
        producer.send(new ProducerRecord<String, String>("newsjsons", jsonStr));
       // producer.close();
        System.out.println("kafka");
    }

}
