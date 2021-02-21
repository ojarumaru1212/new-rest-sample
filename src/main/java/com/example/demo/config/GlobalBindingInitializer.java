//package com.example.demo.config;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Validator;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.support.WebBindingInitializer;
//import org.springframework.web.context.request.WebRequest;
//
//import com.example.demo.validator.HogeValidator;
//
//@ControllerAdvice
//@Component
//public class GlobalBindingInitializer implements WebBindingInitializer {
//	@Autowired
//	private HogeValidator hogeValidator;
//
//	@InitBinder
//	@Override
//	public void initBinder(WebDataBinder binder, WebRequest req) {
//		//Dateの書式を設定します
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
//		dateFormat.setLenient(false);
//		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//	}
//
//	@Override
//	public void initBinder(WebDataBinder binder) {
//		// TODO 自動生成されたメソッド・スタブ
//		binder.setValidator((Validator) hogeValidator);
//
//	}
//
//}
