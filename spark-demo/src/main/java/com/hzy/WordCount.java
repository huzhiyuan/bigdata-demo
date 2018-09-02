package com.hzy;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

public class WordCount {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("hadoop.home.dir","C:\\bigdata\\servers\\hadoop\\hadoop-2.7.6");
        properties.setProperty("java.home","C:\\Program Files\\Java\\jdk1.8.0_161");

        System.setProperties(properties);

        SparkConf sparkConf = new SparkConf().setAppName("wordCount").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaRDD<String> input = sc.textFile("C:\\bigdata\\datas\\anna.txt");

        JavaRDD<String> words = input.flatMap(
                new FlatMapFunction<String, String>() {
                    @Override
                    public Iterator<String> call(String s) throws Exception {
                        return Arrays.asList(s.split(" ")).iterator();
                    }
                }
        );

        //转换为键值对并计数
        JavaPairRDD<String,Integer> counts = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    @Override
                    public Tuple2<String, Integer> call(String s) throws Exception {
                        return new Tuple2<>(s,1);
                    }
                }
        );

        JavaPairRDD<String,Integer> result = counts.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer+integer2;
            }
        });

        result.saveAsTextFile("C:\\bigdata\\datas\\");
    }
}
