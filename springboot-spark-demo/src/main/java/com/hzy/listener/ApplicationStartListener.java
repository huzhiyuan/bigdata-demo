package com.hzy.listener;

import com.hzy.task.KafkaSparkStreamExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 监听器，spring boot 容器加载完成后执行
 * 启动kafka数据接收和处理
 *
 */
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("==========================================================");
        ApplicationContext ac = event.getApplicationContext();
        KafkaSparkStreamExecutor sparkKafkaStreamExecutor= ac.getBean(KafkaSparkStreamExecutor.class);
        Thread thread = new Thread(sparkKafkaStreamExecutor);
        thread.start();
    }

}
