package com.example.demo.form;

import java.io.IOException;

import com.example.demo.form.Hoge.Tags;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class TagsDeserializer extends JsonDeserializer<Tags>{

	@Override
	  public Tags deserialize(JsonParser jsonParser, DeserializationContext ctxt)
	      throws IOException, JsonProcessingException {
		Tags value = null;
		try {
		value  = Tags.value(jsonParser.getValueAsString());
		}catch(Exception ex) {
			MismatchedInputException e = MismatchedInputException.from(jsonParser , Tags.class, ex.getMessage());
			throw e;
		}

	    return value;
	  }

}
