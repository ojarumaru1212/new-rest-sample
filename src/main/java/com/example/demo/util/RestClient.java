package com.example.demo.util;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.Intercept.CustomErrorHandler;
import com.example.demo.Intercept.RestTemplateLoggingInterceptor;
import com.example.demo.client.form.AnotherApiRequestEntityBody;
import com.example.demo.exception.RetryableException;

public class RestClient<T> {

	private final static int secondMetricPrefix = 1000;

	public static class InitialCommunicationSettingsInfo{
		private int retryCount;
		private int retryIntervalSecond;
		private int readTimeoutSecond;
		private int connectionTimeoutSecond;

		public int getRetryCount() {
			return retryCount;
		}
		public void setRetryCount(int retryCount) {
			this.retryCount = retryCount;
		}
		public int getRetryIntervalSecond() {
			return retryIntervalSecond;
		}
		public void setRetryIntervalSecond(int retryIntervalSecond) {
			this.retryIntervalSecond = retryIntervalSecond;
		}
		public int getReadTimeoutSecond() {
			return readTimeoutSecond;
		}
		public void setReadTimeoutSecond(int readTimeoutSecond) {
			this.readTimeoutSecond = readTimeoutSecond;
		}
		public int getConnectionTimeoutSecond() {
			return connectionTimeoutSecond;
		}
		public void setConnectionTimeoutSecond(int connectionTimeoutSecond) {
			this.connectionTimeoutSecond = connectionTimeoutSecond;
		}

	}

	private RetryTemplate retryTemplate;
	private RestTemplate restTemplate;

	private Class<T> type;

	public Class<T> getType() {
        return type;
    }

    public RestClient(InitialCommunicationSettingsInfo initialCommunicationSettingsInfo, @SuppressWarnings("unchecked") T... e) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) e.getClass().getComponentType();
        this.type = type;

        this.retryTemplate = createRetryTemplate(initialCommunicationSettingsInfo);
        this.restTemplate = createRestTemplate(initialCommunicationSettingsInfo);

    }



	private RetryTemplate createRetryTemplate(InitialCommunicationSettingsInfo initialCommunicationSettingsInfo) {

		RetryTemplate retryTemplate = new RetryTemplate();

		Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<Class<? extends Throwable>, Boolean>();
		retryableExceptions.put(RetryableException.class, true);
		int retryCount = initialCommunicationSettingsInfo.getRetryCount() + 1;
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(retryCount, retryableExceptions);
		retryTemplate.setRetryPolicy(retryPolicy);

		ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
		int retryIntervalMillisecond = initialCommunicationSettingsInfo.getRetryIntervalSecond() * secondMetricPrefix;
		exponentialBackOffPolicy.setInitialInterval(retryIntervalMillisecond);
		retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

		return retryTemplate;

	}

	private RestTemplate createRestTemplate(InitialCommunicationSettingsInfo initialCommunicationSettingsInfo) {

		RestTemplate restTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();


		int connectTimeoutMillisecond = initialCommunicationSettingsInfo.getConnectionTimeoutSecond() * secondMetricPrefix;
		int readTimeoutMillisecond = initialCommunicationSettingsInfo.getReadTimeoutSecond() * secondMetricPrefix;

		requestFactory.setConnectTimeout(connectTimeoutMillisecond);
		requestFactory.setReadTimeout(readTimeoutMillisecond);
		restTemplate.setRequestFactory(requestFactory);
		List<ClientHttpRequestInterceptor> interceptors =new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new RestTemplateLoggingInterceptor());
		restTemplate.setInterceptors(interceptors);
		restTemplate.setErrorHandler(new CustomErrorHandler());

		return restTemplate;

	}

	public ResponseEntity<T> reqesutGet(String uri, HttpHeaders headers, AnotherApiRequestEntityBody apiRequestEntityBody) throws Exception {


		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);

		Map<String, String> queryMap = null;
		try {
			queryMap = BeanUtils.describe(apiRequestEntityBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		queryMap.remove("class");
		for (Map.Entry<String, String> query : queryMap.entrySet()) {
			if (Objects.nonNull(query.getValue()) && !query.getValue().isEmpty()) {
				builder.queryParam(query.getKey(), query.getValue());
			}
		}


		URI url = builder.build().toUri();

		RequestEntity<AnotherApiRequestEntityBody> requestEntitiy = new RequestEntity<AnotherApiRequestEntityBody>(null,
				headers, HttpMethod.GET, url, AnotherApiRequestEntityBody.class);

		ResponseEntity<T> responseEntity = reqesutWithRetry(requestEntitiy);

		return responseEntity;

	}

	public ResponseEntity<T> reqesutPost(String uri, HttpHeaders headers, AnotherApiRequestEntityBody apiRequestEntityBody) throws Exception {

		URI url = new URI(uri);

		RequestEntity<AnotherApiRequestEntityBody> requestEntitiy = new RequestEntity<AnotherApiRequestEntityBody>(apiRequestEntityBody,
				headers, HttpMethod.POST, url, AnotherApiRequestEntityBody.class);

		ResponseEntity<T> responseEntity = reqesutWithRetry(requestEntitiy);

		return responseEntity;

	}


	private ResponseEntity<T> reqesutWithRetry(RequestEntity<AnotherApiRequestEntityBody> requestEntitiy ) throws Exception {


		ResponseEntity<T> responseEntity = retryTemplate.execute(new RetryCallback<ResponseEntity<T>>() {
			@Override
			public ResponseEntity<T> doWithRetry(RetryContext context) throws RetryableException {

//				System.out.println(context.getRetryCount());

				ResponseEntity<T> responseEntity = requestHttp(requestEntitiy);
				return responseEntity;
			}
		});

		return responseEntity;

	}

	private ResponseEntity<T> requestHttp(RequestEntity<AnotherApiRequestEntityBody> requestEntitiy)
			throws RetryableException {


		try {
			ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntitiy,
					getType() );

			return responseEntity;

		} catch (ResourceAccessException e) {
			if (e.getCause() instanceof SocketTimeoutException && "connect timed out".equals(e.getCause().getMessage())) {
				throw new RetryableException(e);
			}
			throw e;
		}
	}

}
