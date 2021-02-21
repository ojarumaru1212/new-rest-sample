package com.example.demo.Intercept;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.demo.form.Supper;

public class CommonParametersMethodArgumentResolver implements HandlerMethodArgumentResolver {


	@Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Supper.class.equals(parameter.getParameterType()); // (3)
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
    	Supper params = new Supper(); // (4)
        params.setVersion(webRequest.getHeader("X-id"));
        return params;
    }

}
