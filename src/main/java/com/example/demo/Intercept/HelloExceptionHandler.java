package com.example.demo.Intercept;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.ValitionException;
import com.example.demo.form.ApiValidationErrorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import lombok.Data;

@RestControllerAdvice
public class HelloExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	ObjectMapper objectMapper;

	@ExceptionHandler
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		ApiValidationErrorResponse error = new ApiValidationErrorResponse("validation error");
		ex.getConstraintViolations().forEach(v -> error.addValidationMessage(v.getMessage()));
		HttpHeaders heddars = new HttpHeaders();
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", getTraceId());
		heddars.setAll(values);

		System.out.println("kindId:" + getKindId());

		//		return super.handleExceptionInternal(ex, error, null, HttpStatus.BAD_REQUEST, request);
		return super.handleExceptionInternal(ex, null, heddars, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleViolationException(ValitionException ex,
			WebRequest request) {

		HttpHeaders heddars = new HttpHeaders();
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", getTraceId());
		heddars.setAll(values);

		String parmName = ex.getParmName();
		String parmValue = ex.getParmValue();

		System.out.println("kindId:" + getKindId() +"," + parmName + ":" + parmValue );

		//		return super.handleExceptionInternal(ex, error, null, HttpStatus.BAD_REQUEST, request);
		return super.handleExceptionInternal(ex, null, heddars, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> handleRestException(RestClientResponseException ex,
			WebRequest request) {

		HttpHeaders heddars = new HttpHeaders();
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", getTraceId());
		heddars.setAll(values);

		String statusCode = Objects.toString(ex.getRawStatusCode(),null);
		HttpHeaders apiHeaders = ex.getResponseHeaders();
		String responseBody = ex.getResponseBodyAsString();

		System.out.println("kindId:" + getKindId() +"," + statusCode+ ":" + responseBody );

		//		return super.handleExceptionInternal(ex, error, null, HttpStatus.BAD_REQUEST, request);
		return super.handleExceptionInternal(ex, null, heddars, HttpStatus.BAD_REQUEST, request);
	}


	@ExceptionHandler
	public ResponseEntity<Object> handleAllException(Exception ex,
			WebRequest request) {

		HttpHeaders heddars = new HttpHeaders();
		Map<String, String> values = new LinkedHashMap<String, String>();
		values.put("X-headerId", getTraceId());
		heddars.setAll(values);


		System.out.println("kindId:" + ex.getCause() );

		//		return super.handleExceptionInternal(ex, error, null, HttpStatus.BAD_REQUEST, request);
		return super.handleExceptionInternal(ex, null, heddars, HttpStatus.BAD_REQUEST, request);
	}

	private String getKindId() {

		HttpServletRequest req = getRequest();
		Object kindId = req.getAttribute("kindId");

		return Objects.toString(kindId, null);
	}

	private String getTraceId() {

		HttpServletRequest req = getRequest();
		String traceId = req.getHeader("X-id");

		return traceId;
	}

	private HttpServletRequest getRequest() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return req;
	}


	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		if(ex instanceof HttpMessageNotReadableException) {
			HttpMessageNotReadableException hex = (HttpMessageNotReadableException)ex;
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

	        HttpServletRequest request2 = ((ServletRequestAttributes) requestAttributes).getRequest();

	        HttpHeaders requestHttpHeaders = getHttpHeaders(request2);
	        Charset reqCharset = getCharsetOrDefault(requestHttpHeaders.getContentType());
	        String requestBody = null;
	        try {
	            ServletInputStream sis = request2.getInputStream();
	            byte[] requestBytes = sis.readAllBytes();
	            requestBody = new String(requestBytes, reqCharset);
	        } catch (IOException e) {
	            requestBody = "リクエストボディの読み込みに失敗しました";
	        }

	        Map<String, Object> map = null;
	        try {
				map = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>(){});
			} catch (JsonMappingException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}


			MismatchedInputException mie = (MismatchedInputException) hex.getCause();
			ErrorResponse errorResponse = new ErrorResponse();
			List<Reference> pathList = mie.getPath();

			List<String> pathNameList = new ArrayList<String>();
			for(Reference tmpPath : pathList) {
				pathNameList.add(tmpPath.getFieldName());
			}

			Object obj = foreach(pathList, map);
			String pathNameJoint = String.join(".", pathNameList);
			System.out.println(pathNameJoint +":"+ Objects.toString(obj));

			errorResponse.setErrorFiled(pathNameJoint);
			errorResponse.setErrorValue(obj);

			body = errorResponse;
		}
		headers.add("traceId", "testId");



	    return new ResponseEntity<>(body, headers, status);
	}

	@Data
	public static class ErrorResponse{
		private String id;


		@JsonProperty("form_error_value")
		private Object form;
		private String errorFiled;
		private Object errorValue;
	}

	public static Charset getCharsetOrDefault(MediaType mediaType) {
        if (Objects.nonNull(mediaType) && Objects.nonNull(mediaType.getCharset())) {
            return mediaType.getCharset();
        }

        return StandardCharsets.UTF_8;
    }

	public static HttpHeaders getHttpHeaders(HttpServletRequest request) {
        HttpHeaders requestHttpHeaders = new HttpHeaders();
        Enumeration<String> hedaerNames = request.getHeaderNames();
        while (hedaerNames.hasMoreElements()) {
            String headerName = hedaerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            List<String> headerValueList = Collections.list(headerValues);
            requestHttpHeaders.addAll(headerName, headerValueList);
        }

        return requestHttpHeaders;
    }

	Object foreach(List<Reference> list, Object obj) {
		if(!(obj instanceof Map)) {
			return obj;
		}

		Reference r = list.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj;
		Object subObj = map.get(r.getFieldName());
		Object ans = foreach(list.subList(1, list.size()), subObj);
		return ans;
	}

}
