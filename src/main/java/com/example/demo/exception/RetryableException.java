package com.example.demo.exception;

public class RetryableException extends Exception {
	public RetryableException(Exception ex) {
		super(ex);
	}

	public RetryableException() {
		super();
	}
}
