package com.example.demo.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.client.form.RequestEntitiyJavaBeanBody;
import com.example.demo.client.form.Result;
import com.example.demo.form.JavaBean;
import com.example.demo.util.RestClient;
import com.example.demo.util.RestClient.InitialCommunicationSettingsInfo;
import com.example.demo.util.TestUtil;
import com.example.demo.util.TestUtil.NgHeaderSet;
import com.example.demo.util.TestUtil.OkHeaderSet;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TemplateConfig {

	@Bean
	public RestClient<Result> getRest(){

		InitialCommunicationSettingsInfo initialCommunicationSettingsInfo = new InitialCommunicationSettingsInfo();
		initialCommunicationSettingsInfo.setConnectionTimeoutSecond(1);
		initialCommunicationSettingsInfo.setReadTimeoutSecond(1);
		initialCommunicationSettingsInfo.setRetryCount(3);
		initialCommunicationSettingsInfo.setRetryIntervalSecond(1);

		RestClient<Result> rest = new RestClient<Result>(initialCommunicationSettingsInfo);
		return rest;
	}

	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {

			@Override
			public void configure(ObjectMapper objectMapper) {
				super.configure(objectMapper);

				// ここでObjectMapperに対する設定が書ける
			}
		};

		return builder;
	}

	@Bean
	public ModelMapper modelMapper() {


		PropertyMap<JavaBean, RequestEntitiyJavaBeanBody> personMap = new PropertyMap<JavaBean, RequestEntitiyJavaBeanBody>() {
			protected void configure() {


				using(new Converter<JavaBean, String>(){
					public String convert(MappingContext<JavaBean, String> context) {
						return testConvert(context.getSource().getTime());
					}
				}).map(source, destination.getaAndB());


			}
		};


		ModelMapper modelMapper2 = new ModelMapper();
		modelMapper2 .addMappings(personMap);
		return modelMapper2;
	}

	public String testConvert(String msg) {
		String csv = ",";
		String r = msg.replaceAll(csv, "NEW");


		return r;

	}

	@Bean
	public TestUtil testUtil() {
		var okSet = new OkHeaderSet("orizinalHeader", "Location");

		var ngSet = new NgHeaderSet("x-hheade");
		return new TestUtil(okSet, ngSet);
	}

}
