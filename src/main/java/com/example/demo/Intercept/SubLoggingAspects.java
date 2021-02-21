package com.example.demo.Intercept;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.demo.form.RequestInterface;
import com.example.demo.form.Supper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Aspect
@Component
public class SubLoggingAspects extends AbstractLoggingAspects {

	@Override
	protected void setFormHeader(RequestInterface requestInterface) {
		// TODO 自動生成されたメソッド・スタブ

		Supper su  =(Supper)requestInterface;
		su.setVersion(getRequest().getHeader("X-id"));
		try {
			System.out.println(mapper.writeValueAsString(su));
		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
