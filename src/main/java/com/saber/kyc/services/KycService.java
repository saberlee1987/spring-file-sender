package com.saber.kyc.services;

import com.saber.kyc.dto.KycResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface KycService {
	KycResponseDto passive_auth(String text , MultipartFile index_face, MultipartFile auth_video);
}
