package com.hzy;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class KafkaProducerDemo {
    private  final Producer<String,String> kafkaProducer;

    public final static String TOPIC="logs-test";

    private KafkaProducerDemo(){
        kafkaProducer=createKafkaProducer() ;
    }
    private Producer<String,String> createKafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.150.126:9092,192.168.150.126:9093,192.168.150.126:9094");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> kafkaProducer = new KafkaProducer(props);
        return kafkaProducer;
    }

    void produce(){
        for(int i=1;i<1000;i++){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String key=String.valueOf("key"+i);
            String data="hello kafka message:"+key;
            kafkaProducer.send(new ProducerRecord<>(TOPIC, key, data), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    //do sth
                }
            });
            System.out.println(data);
        }
    }

    public  static  void main(String[] args){
        new KafkaProducerDemo().produce();
    }
}
