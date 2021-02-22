package com.example.demo;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.demo.form.Fuga;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;


/**
 * @author yoshi
 * @SpringBootApplication<br>
 * -> @EnableAutoConfiguration > @Import(AutoConfigurationImportSelector.class) > META-INF/spring.factories. に記載してある<br>
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration を、ライブラリの設定クラスとして読み込む（自動設定）
 */
@Slf4j
@EnableAsync
@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext a = SpringApplication.run(SampleApplication.class, args);
		Gson gson = new Gson();
		log.info("aa");
		Fuga fuga = fugaCreate();
		log.info(gson.toJson(fuga));
	}
	
	public static Fuga fugaCreate() {
		Fuga fuga = new Fuga();
		fuga.setId(11);
		fuga.setStrFuga("fugafuga");
		log.info(ToStringBuilder.reflectionToString(fuga));
		
		return fuga;
	}

}
