package com.jing;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @author RanMoAnRan
 * @ClassName: TestPartitioner
 * @projectName spider-httpclient-jsoup
 * @description: 自定义分区策略类
 * @date 2019/6/14 20:25
 */
public class TestPartitioner implements Partitioner {
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        //自己实现自己的分区逻辑
        System.out.println("进入了自定义分区类");
        return 0;
    }

    public void close() {

    }

    public void configure(Map<String, ?> map) {

    }
}
