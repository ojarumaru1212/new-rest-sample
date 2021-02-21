package com.example.demo.Intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.annotation.KindOf;
import com.example.demo.form.RequestInterface;
import com.example.demo.form.Supper;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Aspect
//@Component
public abstract class AbstractLoggingAspects {

	@Autowired
	protected ObjectMapper mapper;

	private Map<String, String> getRequestMap() throws Throwable {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(Objects.isNull(requestAttributes)) {
			return null;
		}

		HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();
		String method = req.getMethod();
		Map<String, String[]> pars = req.getParameterMap();
		Enumeration<String> headders = req.getHeaderNames();
		String servletPath = req.getServletPath();

		Enumeration<String>  attributes = req.getAttributeNames();
		DispatcherType disTyep = req.getDispatcherType();
		Map<String, String> trailerFields = req.getTrailerFields();
		Principal Principal = req.getUserPrincipal();
		ServletContext ServletContext = req.getServletContext();
		StringBuffer StringBuffer = req.getRequestURL();
//		try {
//			BufferedReader BufferedReader = req.getReader();
//		} catch (Exception e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
//		Collection<Part> parts = req.getParts();
		String PathTranslated = req.getPathTranslated();


		Object date = req.getAttribute("requestDateTime");
		Object kindId = req.getAttribute("kindId");
//		System.out.println(date);
//		System.out.println(kindId);


		Supper supper = (Supper)req.getAttribute("requestArg");
		Map<String, String> map = new HashMap<String, String>();
		map.put("method", method);
		map.put("servletPath", servletPath);
		map.put("kindId", Objects.toString(kindId));
		map.put("requestDateTime", Objects.toString(date));
		map.put("supper", mapper.writeValueAsString(supper));

		while(headders.hasMoreElements()) {
			String headerName = headders.nextElement();

			if(!headerName.startsWith("x-")) {
				continue;
			}

			String headerValue = req.getHeader(headerName);
			map.put(headerName, headerValue);
		}

		return map;
    }
	protected abstract void setFormHeader(RequestInterface requestInterface);

	private Map<String, String> getOppositeRequestMap() throws Throwable {

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(Objects.isNull(requestAttributes)) {
			return null;
		}

		HttpServletRequest req = ((ServletRequestAttributes) requestAttributes).getRequest();
		String method = Objects.toString(req.getAttribute("oppositeApiRequestMethod"), null);




		Object date = req.getAttribute("requestDateTime");
		Object kindId = req.getAttribute("kindId");


		Map<String, String> map = new HashMap<String, String>();
		map.put("method", method);
		map.put("kindId", Objects.toString(kindId));
		map.put("requestDateTime", Objects.toString(date));

		map.put("oppositeApiRetryCount", Objects.toString(req.getAttribute("oppositeApiRetryCount"), null));

		map.put("oppositeApiStatusCode", Objects.toString(req.getAttribute("oppositeApiStatusCode"), null));
		map.put("oppositeApiResponseHeader", Objects.toString(req.getAttribute("oppositeApiResponseHeader"), null));
		map.put("oppositeApiResponsetBody", Objects.toString(req.getAttribute("oppositeApiResponsetBody"), null));

		HttpHeaders headders = (HttpHeaders) req.getAttribute("oppositeApiRequestHeader");
		for(Entry<String,List<String>> header : headders.entrySet()) {
			String headerName = header.getKey();

			if(!headerName.startsWith("x-")) {
				continue;
			}

			String headerValue = String.join(",", header.getValue()) ;
			map.put(headerName, headerValue);
		}

		return map;
    }

	private void setRequestHeader(Supper supper) {

		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> headders = req.getHeaderNames();

		while(headders.hasMoreElements()) {
			String headerName = headders.nextElement();

			if(!headerName.startsWith("x-")) {
				continue;
			}

			String headerValue = req.getHeader(headerName);

			if(headerName.equals("x-id")) {
				supper.setVersion(headerValue);
			}
		}

    }

	protected HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


	}

	private void setSessionId(String id, Optional<RequestInterface> optional) {

		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


		optional.ifPresent(v -> ((Supper)v).setVersion(req.getHeader("X-id")));
		req.setAttribute("requestDateTime", new Date());
		req.setAttribute("kindId", id);
		req.setAttribute("requestArg", optional.orElse(null));

    }


	@Before(value = "execution(* com.example.demo.controller.*.*(..))")
    public void invokeControllerBefore(JoinPoint joinPoint) {
		System.out.println("before start");
		Object[] beforeArgs = joinPoint.getArgs();
		RequestInterface supper = null;
		for(Object beforeArg : beforeArgs) {
			if(beforeArg instanceof RequestInterface) {
				supper = (RequestInterface)beforeArg;
				setFormHeader(supper);
				break;
			}
		}

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();

        KindOf kindOf = targetMethod.getAnnotation(KindOf.class);
        String id = kindOf.id();


		setSessionId(id, Optional.ofNullable(supper));
		System.out.println("before end");
	}

//	@Around(value = "execution(* com.example.demo.controller.*.*(..))")
	public void invokeController(ProceedingJoinPoint joinPoint) throws Throwable{

		System.out.println("aroud start");
		String methodName = joinPoint.getSignature().getName();
		Signature Signature = joinPoint.getSignature();
		Object target = joinPoint.getTarget();

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method targetMethod = methodSignature.getMethod();

		Annotation[] annotions = targetMethod.getAnnotations();
		KindOf kindOf = targetMethod.getAnnotation(KindOf.class);
		String id = kindOf.id();

		Object[] objects = joinPoint.getArgs();

		for (Object object : objects) {
			if(object instanceof Supper) {

				setRequestHeader((Supper)object);
			}
		}

		joinPoint.proceed(objects);
		System.out.println("aroud end");
	}

//	@Around(value = "execution(* com.example.demo.controller.*.*(..))")
//	public ResponseEntity<Object> invokeController(ProceedingJoinPoint joinPoint){
//
//
//		String methodName = joinPoint.getSignature().getName();
//      Signature Signature = joinPoint.getSignature();
//      Object target = joinPoint.getTarget();
//
//		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//		Method targetMethod = methodSignature.getMethod();
//
//		Annotation[] annotions = targetMethod.getAnnotations();
//		KindOf kindOf = targetMethod.getAnnotation(KindOf.class);
//		String id = kindOf.id();
//
//		Object[] objects = joinPoint.getArgs();
//		for (Object object : objects) {
//
//		}
//
//		try {
//			joinPoint.getSignature().getName();
//			Object response = joinPoint.proceed();
//			return (ResponseEntity<Object>) response;
//		} catch (Throwable e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//
//			HttpHeaders heddars = new HttpHeaders();
//			Map<String, String> values = new LinkedHashMap<String, String>();
//			values.put("X-headerId", "header1");
//			heddars.setAll(values);
//
//			return new ResponseEntity<>(null, heddars, HttpStatus.BAD_REQUEST);
//		}
//	}


	@AfterReturning(pointcut = "execution(* com.example.demo.Intercept.HelloExceptionHandler.handle*(..))", returning = "returnValue")
    public void invokeExceptionHadlerAfter(JoinPoint joinPoint, Object returnValue) throws Throwable {
		System.out.println("afterExceptionHadler start");
        log(returnValue);
        System.out.println("afterExceptionHadler end");
    }

	@AfterReturning(pointcut = "execution(* com.example.demo.controller.*.*(..))", returning = "returnValue")
    public void invokeControllerAfter(JoinPoint joinPoint, Object returnValue) throws Throwable {
		System.out.println("afterController start");
        log(returnValue);
        System.out.println("afterController end");
    }

	@Before(value = "execution(* com.example.demo.util.RestClient.*(..))")
    public void invokeRequestApiBefore(JoinPoint joinPoint) throws Throwable {
		System.out.println("RequestApiBefore start");
		String string = joinPoint.toString();
        String args = Arrays.toString(joinPoint.getArgs());

        Object[] objects = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        Signature Signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();


        System.out.println("RequestApiBefore end");
    }

	@AfterReturning(pointcut = "execution(* com.example.demo.util.RestClient.*(..))", returning = "returnValue")
    public void invokeRequestApiAfter(JoinPoint joinPoint, Object returnValue) throws Throwable {
		System.out.println("RequestApiAfter start");
		String string = joinPoint.toString();
        String args = Arrays.toString(joinPoint.getArgs());

        Object[] objects = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        Signature Signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();


        oppositeApiAccessLog(returnValue);
        System.out.println("RequestApiAfter end");
    }

	@AfterThrowing(pointcut = "execution(* com.example.demo.util.RestClient.*(..))", throwing  = "e")
    public void invokeRequestApiExceptionAfter(JoinPoint joinPoint, Throwable e) throws Throwable {
		System.out.println("RequestApiAfter start");
		String string = joinPoint.toString();
        String args = Arrays.toString(joinPoint.getArgs());

        Object[] objects = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        Signature Signature = joinPoint.getSignature();
        Object target = joinPoint.getTarget();


        oppositeApiAccessLog(e);
        System.out.println("RequestApiAfter end");
    }



//	@After(value = "execution(* com.example.demo..*.*.*(..))")
//    public void invokeTestAfter(JoinPoint joinPoint, Object returnValue) throws Throwable {
//		System.out.println("after start");
//		String string = joinPoint.toString();
//        String args = Arrays.toString(joinPoint.getArgs());
//
//        Object[] objects = joinPoint.getArgs();
//        String methodName = joinPoint.getSignature().getName();
//        Signature Signature = joinPoint.getSignature();
//        Object target = joinPoint.getTarget();
//
//
//        log(returnValue);
//        System.out.println("after end");
//    }



	private void oppositeApiAccessLog(Object returnValue) throws Throwable {

		Map<String, String> map = getOppositeRequestMap() ;
		if(Objects.isNull(map)) {
			return;
		}
		String message = "accessLog: " + map.get("kindId") + "," + map.toString() + ", " + mapper.writeValueAsString(returnValue);
		System.out.println(message);

	}

	private void log(Object returnValue) throws Throwable {

		Map<String, String> map = getRequestMap();
		if(Objects.isNull(map)) {
			return;
		}
		String message = "accessLog: " + map.get("kindId") + "," + map.toString() + ", " + mapper.writeValueAsString(returnValue);
		System.out.println(message);

	}

	private void oppositeApiAccessLog(Throwable e) throws Throwable {

		Map<String, String> map = getOppositeRequestMap();
		if(Objects.isNull(map)) {
			return;
		}
		String message = "accessLog: " + map.get("kindId") + "," + map.toString() + ", " + e.getMessage();
		System.out.println(message);

	}

}
