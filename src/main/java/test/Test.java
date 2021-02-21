package test;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.example.demo.client.form.NestA;
import com.example.demo.client.form.NestB;
import com.example.demo.client.form.NestC;
import com.example.demo.client.form.Result;
import com.example.demo.form.ResultJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ObjectMapper objectMapper = new ObjectMapper();

		System.out.println("start");
		Result result = new Result();
		result.setA("reslutA");
		result.setB("reslutB");
		NestA nestA = new NestA();
		NestB nestB = new NestB();
		NestC nestC = new NestC();
		nestC.setName("nestCname");
		nestC.setValue("nestCvalue");
		nestB.setNestC(nestC);
		nestA.setNestB(nestB);
		result.setNestA(nestA);
		result.setSa(11);
		result.setSeki(12);
		result.setSho(13);
		result.setWa(14);

		try {
			System.out.println(objectMapper.writeValueAsString(result));

			ModelMapper modelMapper = new ModelMapper();

			System.out.println();
			ResultJson resultJson = modelMapper.map(result, ResultJson.class);

			System.out.println(objectMapper.writeValueAsString(resultJson));

			PropertyMap<Result, ResultJson> personMap = new PropertyMap<Result, ResultJson>() {
				protected void configure() {
					map().setError(source.getA());
				}
			};

			Converter<Result, ResultJson> converter = context -> {
				ResultJson dto = new ResultJson();
		        dto.setError(
		                context.getSource().getA());

		        dto.setSa(context.getSource().getSa());
		        return dto;
		    };

			ModelMapper modelMapper2 = new ModelMapper();

			//コンバータならできるが、すべてのフィールドを書かないと対応できない
//			modelMapper2.createTypeMap(Result.class, ResultJson.class)
//            .setConverter(converter);

			//
			modelMapper2.addMappings(personMap);

			System.out.println();
			ResultJson resultJson2 = modelMapper2.map(result, ResultJson.class);

			System.out.println(objectMapper.writeValueAsString(resultJson2));
		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
