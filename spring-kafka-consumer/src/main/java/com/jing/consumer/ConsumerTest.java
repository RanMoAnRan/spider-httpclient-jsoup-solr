package com.jing.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author RanMoAnRan
 * @ClassName: ConsumerTest
 * @projectName spider-httpclient-jsoup
 * @description: TODO
 * @date 2019/6/16 17:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:kafka-consumer.xml")
public class ConsumerTest {
    @Test
    public void test() throws IOException {
        //将spring容器启动后,不要停止
        System.in.read();
    }
}
