package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.client.form.RequestEntitiyJavaBeanBody;
import com.example.demo.client.form.Result;
import com.example.demo.form.JavaBean;
import com.example.demo.util.RestClient;

@Service
public class WebSerivce {
	@Autowired
	public RestClient<Result> restClient;

	@Autowired
	private ModelMapper modelMapper;

	public ResponseEntity<Result>  request(JavaBean javaBean) throws Exception {

			RequestEntitiyJavaBeanBody javaBeanBody = modelMapper.map(javaBean, RequestEntitiyJavaBeanBody.class);
//			javaBeanBody.setA(javaBean.getA());
//			javaBeanBody.setB(javaBean.getB());
//			javaBeanBody.setTime(javaBean.getTime());

			String uri =  "http://localhost/api_xml.php?a=" + javaBean.getA() + "&b=" + javaBean.getB();

			if("c".equals(javaBean.getTime())) {
				uri = "http://192.168.253.213/api_xml.php?a=" + javaBean.getA() + "&b=" + javaBean.getB();
			}


			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.TEXT_XML_VALUE);
			headers.set("X-id", "1");
//			return restClient.reqesutGet(javaBean);
			return restClient.reqesutPost(uri, headers, javaBeanBody);


	}


}
