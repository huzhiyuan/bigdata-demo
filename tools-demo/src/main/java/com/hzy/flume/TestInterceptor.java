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


public class TestInterceptor implements Interceptor {

    public static Logger logger = LogManager.getLogger(TestInterceptor.class);

    private TestInterceptor() {
    }


    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        return null;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        return null;
    }

    @Override
    public void close() {

    }
}

