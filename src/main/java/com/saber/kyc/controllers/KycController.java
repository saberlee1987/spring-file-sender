package com.saber.kyc.controllers;

import com.saber.kyc.dto.ErrorResponseDto;
import com.saber.kyc.dto.KycResponseDto;
import com.saber.kyc.services.KycService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "kyc", description = "spring-rest-client")

@RequestMapping(value = "${service.api.base-path}/kyc", produces = MediaType.APPLICATION_JSON_VALUE)
public class KycController {

    private final KycService kycService;

    @PostMapping(value = "/passive_auth", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(tags = {"passive_auth"}, summary = "passive_auth", description = "findByNationalCode api", method = "POST",
            parameters = {
                    @Parameter(name = "text", in = ParameterIn.QUERY, required = true, example = "سلام من صابر عزیزی هستم", description = "توضیحات")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = KycResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "403", description =  "FORBIDDEN",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "404", description =  "NOT_FOUND",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "503", description = "SERVICE_UNAVAILABLE",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "504", description = "GATEWAY_TIMEOUT",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))}),

    })
    public ResponseEntity<KycResponseDto> passive_auth(@RequestParam(value = "index_face")
                                                     @Valid @NotNull(message = "index_face is Required")
                                                             MultipartFile indexFace,
                                                     @RequestParam(value = "auth_video") @Valid @NotNull(message = "auth_video is Required")
                                                             MultipartFile authVideo,
                                                     @RequestParam(name = "text") @Valid @NotBlank(message = "text is Required") String text) {
        KycResponseDto response = kycService.passive_auth(text, indexFace, authVideo);
        log.info("response ====> {}", response);
        return ResponseEntity.ok(response);
    }
}
