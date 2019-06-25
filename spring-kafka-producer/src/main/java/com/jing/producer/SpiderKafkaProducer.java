package com.jing.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author RanMoAnRan
 * @ClassName: SpiderKafkaProducer
 * @projectName spider-httpclient-jsoup
 * @description: kafka生产者
 * @date 2019/6/16 17:09
 */
@Component
public class SpiderKafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;


    public void sendSpider(String news) {
        kafkaTemplate.send("spider-test",news);
    }

}
