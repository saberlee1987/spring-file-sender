package com.saber.kyc.dto;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

@Data
public class ParamDto {
	private String text;
	private String token;
	
	@Override
	public String toString() {
		return new GsonBuilder()
				.enableComplexMapKeySerialization()
				.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.create().toJson(this, ParamDto.class);
	}
}
