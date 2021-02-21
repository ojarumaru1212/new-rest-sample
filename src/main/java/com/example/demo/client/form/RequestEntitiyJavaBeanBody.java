package com.example.demo.client.form;

public class RequestEntitiyJavaBeanBody implements AnotherApiRequestEntityBody {

	private Integer a;

	private String b;

	private String time;

	private String aAndB;

	public String getaAndB() {
		return aAndB;
	}

	public void setaAndB(String aAndB) {
		this.aAndB = aAndB;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

}
