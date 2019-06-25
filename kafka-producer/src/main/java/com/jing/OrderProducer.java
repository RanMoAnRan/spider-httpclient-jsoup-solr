package com.jing;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author RanMoAnRan
 * @ClassName: OrderProducer
 * @projectName spider-httpclient-jsoup
 * @description: kafka的生产者
 * @date 2019/6/14 17:58
 */
public class OrderProducer {
    public static void main(String[] args) {

        Properties props = new Properties();
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


        //自定义分区策略类
        props.put("partitioner.class","com.jing.TestPartitioner");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 100; i++) {

            //指定了partition和key则key失效将数据，写入到指定的partition分区中
            //ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("order", 2, Integer.toString(i), Integer.toString(i));

            //没有指定partition，制定了key，数据分发策略为指定的key求 hash%分区数来分配数据
            // ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("order", Integer.toString(i), Integer.toString(i));

            //既没指定partition也没key则 分发策略为轮询
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("order", Integer.toString(i));
            producer.send(producerRecord);
        }

        producer.close();

    }
}
