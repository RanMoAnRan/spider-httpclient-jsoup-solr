package com.jing.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author RanMoAnRan
 * @ClassName: ProducerTest
 * @projectName spider-httpclient-jsoup
 * @description: TODO
 * @date 2019/6/16 17:21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:kafka-producer.xml")
public class ProducerTest {
    @Autowired
    SpiderKafkaProducer spiderKafkaProducer;

    @Test
    public void test(){
        spiderKafkaProducer.sendSpider("靖哥最帅");
    }
}
