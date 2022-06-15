package com.saber.kyc.dto;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

@Data
public class KycResponse {
    private String face_verification;
    private String index_blurriness;
    private String liveness;
    private String speech_similarity;

    private String errorCode;
    private String errorMessage;
    private String systemErrorMessage;

    @Override
    public String toString() {
        return new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create().toJson(this, KycResponse.class);
    }
}
