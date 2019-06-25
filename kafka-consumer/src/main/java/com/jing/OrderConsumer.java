package com.jing;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author RanMoAnRan
 * @ClassName: OrderConsumer
 * @projectName spider-httpclient-jsoup
 * @description: kafka的消费者
 * @date 2019/6/14 18:24
 */
public class OrderConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        //消费组id(必须唯一)
        props.put("group.id", "jing");
        //消费完数据,提交消费数据的偏移量
        props.put("enable.auto.commit", "true");
        //提交偏移量的间隔周期
        props.put("auto.commit.interval.ms", "1000");
        //key和value的序列化方式
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //订阅topic的消息(可以同时订阅多个topic的消息)
        consumer.subscribe(Arrays.asList("order"));
        //消费数据
        while (true) {
            //拉取消息
            ConsumerRecords<String, String> records = consumer.poll(100);
            //遍历 获取消费的偏移量   消息的key   消息的value
            for (ConsumerRecord<String, String> record : records) {
                int partition = record.partition();
                System.out.println("partition = " + partition + " , offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
            }
        }
    }
}
