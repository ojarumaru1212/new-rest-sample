package com.example.demo.config;

import java.util.List;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.example.demo.Intercept.CommonParametersMethodArgumentResolver;

public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {

	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CommonParametersMethodArgumentResolver());
    }

}
