package com.example.demo.form;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class Hoge extends Supper {
	private Integer id;

	@NotNull
	@Size(min = 1, max = 5)
	private String name;

	private Fuga fuga;

	private List<Fuga> fugas;

	@JsonProperty("t_flg")
	private Boolean flg;

	private Tags tags;

	@AllArgsConstructor
	@Getter
	public static enum Tags{
		OK("00"),
		NG("10"),
		NON("99");


		private String code;

		public static Tags value(String code) {
	        // idとマッチするFluitCategoryオブジェクトがない場合独自例外を送出
			return Arrays.stream(values()).filter(x -> x.code.equals(code)).findFirst().orElseThrow(() -> new NullPointerException());
	    }

	}

}
