package com.example.demo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.example.demo.annotation.HogeAno;

@Component
public class HogeValidator  implements ConstraintValidator<HogeAno, String> {

	@Override
    public void initialize(HogeAno constraint) {
		System.out.println("initialize");
    }

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("isValid");
		return false;
	}

}
