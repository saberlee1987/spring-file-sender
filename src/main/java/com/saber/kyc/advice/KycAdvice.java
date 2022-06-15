package com.saber.kyc.advice;

import com.saber.kyc.dto.ErrorResponseDto;
import com.saber.kyc.dto.ValidationDto;
import com.saber.kyc.exceptions.GatewayException;
import com.saber.kyc.exceptions.KycException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class KycAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<ErrorResponseDto> gatewayException(GatewayException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(2);
        errorResponseDto.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponseDto.setOriginalMessage(String.format("{\"code\":%d,\"text\":\"%s\"}",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
                , ex.getMessage()
        ));

        log.error("Error ===> {} ", errorResponseDto);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }

    @ExceptionHandler(KycException.class)
    public ResponseEntity<ErrorResponseDto> kycException(KycException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(2);
        errorResponseDto.setMessage(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
        errorResponseDto.setOriginalMessage(ex.getErrorResponse().toString());
        log.error("Error ===> {} ", errorResponseDto);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponseDto);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {

        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setCode(4);
        errorResponse.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        List<ValidationDto> validationDtoList = new ArrayList<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            ValidationDto validationDto = new ValidationDto();
            validationDto.setFieldName(violation.getPropertyPath().toString());
            validationDto.setConstraintMessage(violation.getMessage());
            validationDtoList.add(validationDto);
        }
        errorResponse.setValidationDetails(validationDtoList);

        log.error("Error for  handleConstraintViolationException with body ===> {}", errorResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setCode(4);
        errorResponse.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        List<ValidationDto> validationDtoList = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            ValidationDto validationDto = new ValidationDto();
            validationDto.setFieldName(fieldError.getField());
            validationDto.setConstraintMessage(fieldError.getDefaultMessage());
            validationDtoList.add(validationDto);
        }
        errorResponse.setValidationDetails(validationDtoList);

        log.error("Error for  handleMethodArgumentNotValid with body ===> {}",  errorResponse);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
