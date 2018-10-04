package com.hzy.flume;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HdfsHeaders implements Interceptor {

    public static Logger logger = LogManager.getLogger(HdfsHeaders.class);

    private HdfsHeaders() {
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
        logger.info("*********************************************************************");

        String body = new String(event.getBody(),Charsets.UTF_8);
        logger.info(body);
        String realBody = body.substring(body.indexOf("{"),body.lastIndexOf("}")+1);
        JSONObject jsonObject1 = JSONObject.parseObject(realBody);
        String logType = jsonObject1.getString("logType");
        logger.warn("=============================================================logType="+logType);

        Map<String, String> headers = event.getHeaders();
        headers.put("logType",logType);
        event.setHeaders(headers);
        event.setBody(realBody.getBytes());
        return event;
    }

    /**
     * 按照监控文件名将日志分类
     * @param events
     * @return
     */
    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> results = new ArrayList<>();
        Event event;
        for (Event e : events) {
            event = intercept(e);
            if (event != null) {
                results.add(event);
            }
        }
        return results;
    }

    public static class Builder implements Interceptor.Builder {
        //使用Builder初始化Interceptor
        @Override
        public Interceptor build() {
            return new HdfsHeaders();
        }

        @Override
        public void configure(Context context) {

        }
    }

}

