package com.hzy;

import java.util.*;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.StreamingContext;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.dstream.InputDStream;
import org.apache.spark.streaming.kafka010.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import scala.Tuple2;


public class KafkaSparkDemo {
    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.setProperty("hadoop.home.dir","C:\\bigdata\\servers\\hadoop\\hadoop-2.7.6");
//        properties.setProperty("java.home","C:\\Program Files\\Java\\jdk1.8.0_161");

//        System.setProperties(properties);
        String group = "something";
        String topics = "test-music-topic";

        SparkConf conf = new SparkConf().setAppName("pvuv").setMaster("local[3]");
        SparkContext sc = new SparkContext(conf);
        StreamingContext ssc = new StreamingContext(sc, new Duration(1000));
        ssc.checkpoint("C:\\bigdata\\bigdata-demo\\spark-demo\\checkpoint");

        Collection<String> topicSets = Arrays.asList(topics.split(","));
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "test");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        InputDStream stream = KafkaUtils.createDirectStream(
                ssc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topicSets, kafkaParams)
        );
        //TODO
        stream.print();

        ssc.start();
        ssc.awaitTermination();
    }
}
