package com.hzy.helper;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class BroadcastHelper implements Serializable{
    private static final long serialVersionUID = 1L;

    @Autowired
    private transient JavaSparkContext jsc;

    @Autowired
    private RedisHelper redisHelper;

    private transient Broadcast broadcast =null;

    @PostConstruct
    public void init(){
        broadcast = jsc.broadcast(null);
    }

    public void update() {
        // 删除RDD是否需要锁定
        System.out.println("更新共享变量");
        broadcast.unpersist(false);
        Map newMap = redisHelper.updateMap();
        broadcast = jsc.broadcast(newMap);
    }
}
