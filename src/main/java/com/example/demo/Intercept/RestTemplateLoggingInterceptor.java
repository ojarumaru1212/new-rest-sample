package com.example.demo.Intercept;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {


	@Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    	HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


    	Object retryCountObj = req.getAttribute("oppositeApiRetryCount");
		int retryCount = Objects.nonNull(retryCountObj) ? (int) retryCountObj : 0;


		req.setAttribute("oppositeApiRetryCount", ++retryCount);

		// リクエストの中身をログに落とす
        System.out.println("RestTemplate Request: URI={}, Headers={}, Body={},"+
                request.getURI()+", "+
                request.getHeaders()+", "+
                new String(body));
        URI requestUri = request.getURI();
        HttpHeaders headers = request.getHeaders();
        String requestBody = new String(body);

        req.setAttribute("oppositeApiRequestHeader", headers);
        req.setAttribute("oppositeApiRequestBody", requestBody);
        req.setAttribute("oppositeApiRequestMethod", request.getMethodValue());



        // レスポンスを取得する
        ClientHttpResponse response = new BufferingClientHttpResponseWrapper(execution.execute(request, body));

        // レスポンスのボディを読み込む
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }

        // レスポンスの中身をログに落とす
        System.out.println("RestTemplate Response: StatusCode={} {}, Headers={}, Body={},"+
                response.getStatusCode()+", "+
                response.getStatusText()+", "+
                response.getHeaders()+", "+
                inputStringBuilder.toString()
        );

        req.setAttribute("oppositeApiStatusCode", response.getStatusCode().value());
        req.setAttribute("oppositeApiResponseHeader", response.getHeaders());
        req.setAttribute("oppositeApiResponsetBody", inputStringBuilder.toString());

        return response;
    }

    private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

        private final ClientHttpResponse response;

        @Nullable
        private byte[] body;


        BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }


        @Override
        public HttpStatus getStatusCode() throws IOException {
            return this.response.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.getStatusText();
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.response.getHeaders();
        }

        @Override
        public InputStream getBody() throws IOException {
            if (this.body == null) {
                this.body = StreamUtils.copyToByteArray(this.response.getBody());
            }
            return new ByteArrayInputStream(this.body);
        }

        @Override
        public void close() {
            this.response.close();
        }

    }

}
