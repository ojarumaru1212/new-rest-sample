package com.example.demo;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@EnableAsync
@SpringBootApplication
public class DemoRestApiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext a = SpringApplication.run(DemoRestApiApplication.class, args);
		ObjectMapper objectMapper =a.getBean(ObjectMapper.class);
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

	}


	@Bean("Thread2") // ここで設定した"Thread2"を＠Asyncに設定するとその設定が利用される
    public Executor taskExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // デフォルトのThreadのサイズ。あふれるとQueueCapacityのサイズまでキューイングする
        executor.setQueueCapacity(1); // 待ちのキューのサイズ。あふれるとMaxPoolSizeまでThreadを増やす
        executor.setMaxPoolSize(500); // どこまでThreadを増やすかの設定。この値からあふれるとその処理はリジェクトされてExceptionが発生する
        executor.setThreadNamePrefix("Thread2--");
        executor.initialize();
        return executor;
    }

}
