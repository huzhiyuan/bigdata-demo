package com.hzy.flume;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.hzy.log.LogHelper;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogAnalysis implements Interceptor {

    public static Logger logger = LogManager.getLogger(LogAnalysis.class);

    private LogAnalysis() {
    }

    @Override
    public void initialize() {
        // NO-OP...
    }

    @Override
    public void close() {
        // NO-OP...
    }

    @Override
    public Event intercept(Event event) {
        if(event==null||event.getHeaders()==null){
            return event;
        }
        Map<String, String> headers = event.getHeaders();
        JSONObject jsonObject = new JSONObject();
        for(String key:headers.keySet()){
            logger.info("headers:========"+key+"====="+headers.get(key));
            jsonObject.put(key,headers.get(key));
        }
        String body = new String(event.getBody(),Charsets.UTF_8);
        jsonObject.put("body",body);
        event.setBody(jsonObject.toJSONString().getBytes());
        return event;
    }

    /**
     * 按照监控文件名将日志分类
     * @param events
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        if(events==null||events.size()==0){
            return events;
        }
        HashMap<String,List<Event>> fileEvents = new HashMap<>();
        for(Event event:events) {
            String filePath = event.getHeaders().get("file");
            if (fileEvents.containsKey(filePath)) {
                List<Event> fileEventList = fileEvents.get(filePath);
                fileEventList.add(event);
                fileEvents.put(filePath,fileEventList);
            } else {
                List<Event> fileEventList = new ArrayList<>();
                fileEventList.add(event);
                fileEvents.put(filePath, fileEventList);
            }
        }
        List<Event> result = Lists.newArrayListWithCapacity(fileEvents.size());

        for(String key:fileEvents.keySet()){
            logger.debug(key+":======:"+fileEvents.get(key).size());
            List<Event> categoryEvents = fileEvents.get(key);
            Event first = categoryEvents.get(0);
            StringBuilder body=new StringBuilder(new String(first.getBody(), Charsets.UTF_8));
            for(int i=1;i<categoryEvents.size();i++){
                Event next = categoryEvents.get(i);
                body.append("\n").append(new String(next.getBody(), Charsets.UTF_8));
            }
            first.setBody(body.toString().getBytes());
            result.add(intercept(first));
        }

        return result;
    }

    public List<Event> intercept2(List<Event> events) {
        if(events==null||events.size()==0){
            return events;
        }
        int originalEventSize = events.size();
        int batchedSize = originalEventSize/20+1;
        List<Event> result = Lists.newArrayListWithCapacity(batchedSize);
        List<List<Event>> xx = averageAssign(events,batchedSize);
        for(List<Event> x:xx){
            Event first = x.get(0);
            StringBuilder body=new StringBuilder(new String(first.getBody(), Charsets.UTF_8));

            for(int i=1;i<x.size();i++){
                Event next = x.get(i);
                body.append("\n").append(new String(next.getBody(), Charsets.UTF_8));
            }
            first.setBody(body.toString().getBytes());

            result.add(intercept(first));
        }

        return result;
    }

    public static class Builder implements Interceptor.Builder {
        //使用Builder初始化Interceptor
        @Override
        public Interceptor build() {
            return new LogAnalysis();
        }

        @Override
        public void configure(Context context) {

        }
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source,int n){
        List<List<T>> result=new ArrayList<List<T>>();
        int remaider=source.size()%n;  //(先计算出余数)
        int number=source.size()/n;  //然后是商
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value=null;
            if(remaider>0){
                value=source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value=source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }

}

