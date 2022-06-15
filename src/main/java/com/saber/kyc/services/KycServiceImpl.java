package com.saber.kyc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saber.kyc.dto.KycErrorResponse;
import com.saber.kyc.dto.KycResponse;
import com.saber.kyc.dto.KycResponseDto;
import com.saber.kyc.dto.ParamDto;
import com.saber.kyc.exceptions.GatewayException;
import com.saber.kyc.exceptions.KycException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class KycServiceImpl implements KycService {
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    @Override
    public KycResponseDto passive_auth(String text, MultipartFile index_face, MultipartFile auth_video) {
        KycResponse kycResponse = null;
        try {
            byte[] index_faceBytes = index_face.getBytes();
            byte[] auth_videoBytes = auth_video.getBytes();
            ParamDto paramDto = new ParamDto();
            paramDto.setText(text);
            paramDto.setToken("0c537df4d1ce411990e7022f6ad5134e");
            HttpEntity httpEntity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addTextBody("param", paramDto.toString(), ContentType.TEXT_PLAIN)
                    .addBinaryBody("index_face", index_faceBytes, ContentType.MULTIPART_FORM_DATA, "index_face")
                    .addBinaryBody("auth_video", auth_videoBytes, ContentType.MULTIPART_FORM_DATA, "auth_video")
                    .build();

            HttpPost httpPost = new HttpPost("http://tejaratkyc.cicap.ir/passive_auth");
            httpPost.setEntity(httpEntity);

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            InputStream content = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String responseBody = reader.readLine();
            log.info("statusCode ====> {}", statusCode);
            log.info("response body ===> {}", responseBody);

            kycResponse = mapper.readValue(responseBody, KycResponse.class);

        } catch (Exception ex) {
            throw new GatewayException(ex.getMessage(), ex);
        }

        if (kycResponse.getErrorCode() != null) {
            KycErrorResponse errorResponse = new KycErrorResponse();
            errorResponse.setErrorCode(kycResponse.getErrorCode());
            errorResponse.setErrorMessage(kycResponse.getErrorMessage());
            errorResponse.setSystemErrorMessage(kycResponse.getSystemErrorMessage());
            throw new KycException(errorResponse);
        }
        KycResponseDto responseDto = new KycResponseDto();
        responseDto.setLiveness(kycResponse.getLiveness());
        responseDto.setFace_verification(kycResponse.getFace_verification());
        responseDto.setSpeech_similarity(kycResponse.getSpeech_similarity());
        responseDto.setIndex_blurriness(kycResponse.getIndex_blurriness());
        return responseDto;

    }
}
