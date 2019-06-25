package com.jing.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author RanMoAnRan
 * @ClassName: SpiderKafkaConsumer
 * @projectName spider-httpclient-jsoup
 * @description: 消费者消息监听者类
 * @date 2019/6/16 17:30
 */
@Component
public class SpiderKafkaConsumer implements MessageListener<Integer,String> {
    public void onMessage(ConsumerRecord<Integer, String> record) {
        //消费者的业务逻辑
        int partition = record.partition();
        System.out.println("partition = " + partition  + " , offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
    }
}
