package com.example.demo.form;

import java.io.Serializable;

public class ValidationMessage implements Serializable {
    private String message;

    public ValidationMessage(String message) {

    	this.message = message;
    }

    public String getMessage() {

    	return message;
    }
}