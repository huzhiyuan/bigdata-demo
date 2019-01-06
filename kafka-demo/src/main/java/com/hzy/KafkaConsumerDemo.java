package com.hzy;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerDemo {
    private  final KafkaConsumer<String, String> consumer;
    public final static String TOPIC="log-topic";

    private KafkaConsumerDemo(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.105:9092,192.168.1.105:9093,192.168.1.105:9094");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
    }

    void consume(){
        consumer.subscribe(Arrays.asList(TOPIC));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
                System.out.println("================================================================");
                //System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                String value = record.value().toString();
                System.out.println(value);
//                JSONObject jsonObject = JSONObject.parseObject(record.value());
//                String logType = jsonObject.getString("logType");
//                String body = jsonObject.getString("body");
//                if("warn".equals(logType)){
//                    if(body.toLowerCase().contains("error")){
//                        System.out.println(logType);
//                        System.out.println(body);
//                    }else{
//                        System.out.println("ok");
//                    }
//                }else if("error".equals(logType)){
//                    if(body.toLowerCase().contains("warn")){
//                        System.out.println(logType);
//                        System.out.println(body);
//                    }else{
//                        System.out.println("ok");
//                    }
//                }else{
//                    System.out.println("unknow type");
//                }
            }
        }
    }

    public  static  void main(String[] args){
        new KafkaConsumerDemo().consume();
    }
}
