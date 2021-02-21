package com.example.demo.controller;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.ValitionException;
import com.example.demo.annotation.KindOf;
import com.example.demo.client.form.NestA;
import com.example.demo.client.form.NestB;
import com.example.demo.client.form.NestC;
import com.example.demo.client.form.Result;
import com.example.demo.form.Hoge;
import com.example.demo.form.JavaBean;
import com.example.demo.form.ResultJson;
import com.example.demo.form.Supper;
import com.example.demo.service.WebSerivce;
import com.example.demo.util.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Validated
public class WebController {

	@Autowired
	private WebSerivce webSerivce;

	@Autowired
	private Optional<TestUtil> testUtilOptional;

	@RequestMapping("/web")
	public String hello() {
		return "hello()";
	}

	@GetMapping("/hoge")
	@KindOf(id = "HOG03", name = "/hoge_get")
	public Hoge getHoge(@ModelAttribute Hoge hoge, HttpServletRequest request) {

		testUtilOptional.ifPresent(testUtil -> {
			testUtil.doSay();
		});

		Enumeration<String> hedaerNames = request.getHeaderNames();
		while (hedaerNames.hasMoreElements()) {

			String headerName = hedaerNames.nextElement();

			Optional<Boolean> isNgOpt = testUtilOptional.map(testUtil -> testUtil.isNgHeader(headerName));
			if(isNgOpt.orElse(false)) {
				continue;
			}


			Optional<Boolean> isOkOpt = testUtilOptional.map(testUtil -> testUtil.isOkHeader(headerName));

			if (headerName.toLowerCase().startsWith("x-") || isOkOpt.orElse(false)) {

				System.out.println(headerName +":" + request.getHeader(headerName));

			}
		}

		if (Objects.isNull(hoge)) {
			hoge = new Hoge();
		}

		String version = request.getHeader("X-id");
		hoge.setVersion(version);

		return hoge;
	}

	@PostMapping("/supper")
	@KindOf(id = "SUP02", name = "/supper_post")
	public Supper postSupper(HttpServletRequest request, Supper test) {

		return test;
	}

	@PostMapping("/hoge")
	@KindOf(id = "HOG02", name = "/hoge_post")
	public ResponseEntity<Supper> postHoge(@RequestBody Hoge hoge) throws Exception {

		if (Objects.isNull(hoge)) {
			hoge = new Hoge();
		}

		if ("ng".equals(hoge.getName())) {
			throw new ValitionException("name", hoge.getName());
		}

		HttpHeaders heddars = new HttpHeaders();
		heddars.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", hoge.getVersion());
		heddars.setAll(values);

		ResponseEntity<Supper> hogeResponse = new ResponseEntity<Supper>(hoge, heddars, HttpStatus.OK);

		return hogeResponse;
	}

	@GetMapping("/valid")
	@KindOf(id = "CON01", name = "/con_get")
	// @Emailと@Validを追加
	public ResponseEntity<Supper> get(@Email @Valid @RequestParam("email") String email, WebRequest request,
			HttpServletRequest request2) {

		String xId = request.getHeader("X-id");

		Hoge hoge = new Hoge();
		hoge.setId(1);
		hoge.setName(email);
		hoge.setVersion(xId);

		HttpHeaders heddars = new HttpHeaders();
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", hoge.getVersion());
		heddars.setAll(values);

		ResponseEntity<Supper> hogeResponse = new ResponseEntity<Supper>(hoge, heddars, HttpStatus.OK);

		return hogeResponse;
	}

	@GetMapping("/get_xml")
	@KindOf(id = "XML01", name = "/get_xml_api")
	public ResponseEntity<Supper> sendXmlApi(@ModelAttribute JavaBean javaBean) throws Exception {

		if ("ng".equals(javaBean.getA())) {
			throw new ValitionException("a", javaBean.getA());
		}

		ResponseEntity<Result> responseEntity = webSerivce.request(javaBean);
		Result result = (Result) responseEntity.getBody();
		//		int statusCoe = responseEntity.getStatusCodeValue();
		ModelMapper modelMapper = new ModelMapper();
		ResultJson resultJson = modelMapper.map(result, ResultJson.class);

		HttpHeaders heddars = new HttpHeaders();
		heddars.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", javaBean.getVersion());
		heddars.setAll(values);

		ResponseEntity<Supper> hogeResponse = new ResponseEntity<Supper>(resultJson, heddars, HttpStatus.OK);

		return hogeResponse;
	}

	@GetMapping("/model")
	@KindOf(id = "MOD01", name = "/model")
	public ResponseEntity<Supper> modelMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		System.out.println("start");
		Result result = new Result();
		result.setA("reslutA");
		result.setB("reslutB");
		NestA nestA = new NestA();
		NestB nestB = new NestB();
		NestC nestC = new NestC();
		nestC.setName("nestCname");
		nestC.setValue("nestCvalue");
		nestB.setNestC(nestC);
		nestA.setNestB(nestB);
		result.setNestA(nestA);
		result.setSa(11);
		result.setSeki(12);
		result.setSho(13);
		result.setWa(14);

		try {
			System.out.println(objectMapper.writeValueAsString(result));

			ModelMapper modelMapper = new ModelMapper();

			System.out.println();
			ResultJson resultJson = modelMapper.map(result, ResultJson.class);

			System.out.println(objectMapper.writeValueAsString(resultJson));

			PropertyMap<Result, ResultJson> personMap = new PropertyMap<Result, ResultJson>() {
				protected void configure() {
					map().setError(source.getA());
				}
			};

			Converter<Result, ResultJson> converter = context -> {
				ResultJson dto = new ResultJson();
				dto.setError(
						context.getSource().getA());

				dto.setSa(context.getSource().getSa());
				return dto;
			};

			ModelMapper modelMapper2 = new ModelMapper();

			//コンバータならできるが、すべてのフィールドを書かないと対応できない
			//			modelMapper2.createTypeMap(Result.class, ResultJson.class)
			//            .setConverter(converter);

			//
			modelMapper2.addMappings(personMap);

			System.out.println();
			ResultJson resultJson2 = modelMapper2.map(result, ResultJson.class);

			System.out.println(objectMapper.writeValueAsString(resultJson2));
		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return null;
	}

}
