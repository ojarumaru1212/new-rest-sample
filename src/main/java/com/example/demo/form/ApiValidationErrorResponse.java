package com.example.demo.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiValidationErrorResponse implements Serializable {

    private String message;

    public String getMessage() {

    	return message;
    }


    public ApiValidationErrorResponse(String message) {
    	this.message = message;

    }

//    @Getter
//    @RequiredArgsConstructor
//    public static class ValidationMessage implements Serializable {
//        private String message;
//
//        public ValidationMessage(String message) {
//
//        	this.message = message;
//        }
//
//        public String getMessage() {
//
//        	return message;
//        }
//    }


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationMessage> validationMessages = new ArrayList<>();

    public void addValidationMessage(String message) {
        validationMessages.add(new ValidationMessage(message));
    }
}
