package com.example.demo.Intercept;

import java.io.IOException;
import java.util.List;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

public class CustomErrorHandler implements ResponseErrorHandler  {


    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public void handleError(ClientHttpResponse response) throws IOException {

        List<String> customHeader = response.getHeaders().get("x-app-err-id");

        String svcErrorMessageID = "";
        if (customHeader != null) {
            svcErrorMessageID = customHeader.get(0);
        }

        try {

//            errorHandler.handleError(response);

        } catch (RestClientException scx) {

            throw scx;
        }
    }

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
    }

}
