package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.form.Hoge.Tags;
import com.example.demo.form.TagsDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class ModelMapperConfig {

	//	@Bean
	//    TypeMapConfigurer<ResultJson, Result> typeMap() {
	//        return new TypeMapConfigurer<ResultJson, Result>() {
	//            @Override
	//            public void configure(TypeMap<ResultJson, Result> typeMap) {
	//                typeMap.addMapping(ResultJson::getError, Result::setNestA);
	//            }
	//        };
	//    }

	@Bean
	public ObjectMapper jsonObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Tags.class, new TagsDeserializer());
		mapper.registerModule(simpleModule);
		return mapper;
	}

}
