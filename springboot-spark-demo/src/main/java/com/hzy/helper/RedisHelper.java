package com.hzy.helper;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class RedisHelper implements Serializable{
    private static final long serialVersionUID = 1L;

    private int updateFlag=0;

    private HashMap<String,String> bigHashMap = new HashMap<String,String>();

    @PostConstruct
    private void init(){
        for (int i = 0; i < 5; i++) {
            bigHashMap.put(String.valueOf(i),"value"+i);
        }
    }

    public HashMap<String,String> getMap(String... args){
        return bigHashMap;
    }

    public HashMap<String,String> updateMap(){
        updateFlag = updateFlag+1;
        bigHashMap.clear();
        for (int i = 0; i < 5; i++) {
            bigHashMap.put(String.valueOf(i),"第"+updateFlag+"次updated value"+i);
        }
        return bigHashMap;
    }
}
