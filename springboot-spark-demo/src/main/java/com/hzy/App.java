package com.hzy;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * link: https://blog.csdn.net/swjtu_yhz/article/details/79361472
 *
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    //将Gson划归为spring管理, 用于对象的序列化和反序列化
    @Bean
    public Gson gson() {
        return new Gson();
    }
}
