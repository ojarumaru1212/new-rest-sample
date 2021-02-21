package com.example.demo.client.form;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result implements AnotherApiResponseEntity {

	private String a ="0";
	private String b ="0";

	private Integer Wa = 0;
	private Integer Sa = 0;
	private Integer Seki = 0;
	private Integer Sho = 0;

	private NestA nestA;


	public NestA getNestA() {
		return nestA;
	}

	public void setNestA(NestA nestA) {
		this.nestA = nestA;
	}

	@XmlAttribute
	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	@XmlAttribute
	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}



	public Integer getWa() {
		return Wa;
	}

	public void setWa(Integer wa) {
		Wa = wa;
	}

	public Integer getSa() {
		return Sa;
	}

	public void setSa(Integer sa) {
		Sa = sa;
	}

	public Integer getSeki() {
		return Seki;
	}

	public void setSeki(Integer seki) {
		Seki = seki;
	}

	public Integer getSho() {
		return Sho;
	}

	public void setSho(Integer sho) {
		Sho = sho;
	}

}
