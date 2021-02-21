package com.example.demo;

public class ValitionException extends Exception {

	private String parmName;
	private String parmValue;

	public ValitionException() {

	}

	public ValitionException(String parmName, String parmValue) {
		this.parmName = parmName;
		this.parmValue = parmValue;
	}

	public String getParmName() {
		return parmName;
	}

	public String getParmValue() {
		return parmValue;
	}

}
