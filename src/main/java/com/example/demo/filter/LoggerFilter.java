package com.example.demo.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class LoggerFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Before!!");
//		chain.doFilter(request, response);
		MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
	    chain.doFilter(multiReadRequest, response);
		System.out.println(response.getOutputStream().toString());

		HttpServletResponse httpServletResponse = ((HttpServletResponse) response);

		System.out.println(httpServletResponse.getStatus());
		Collection<String> h = httpServletResponse.getHeaderNames();
		h.forEach(hv -> System.out.println(hv + ":" + httpServletResponse.getHeader(hv)));
		ServletOutputStream servletOutputStream  = httpServletResponse.getOutputStream();

//		PrintWriter PrintWriter = httpServletResponse.getWriter();
//		PrintWriter.println();

		System.out.println("After!!");

	}

}
