package com.example.demo.form;

public class ResultJson extends Supper{

	private Integer Wa;
	private Integer Sa;
	private Integer Seki;
	private Integer Sho;
	private String Error;


	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
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
