package com.saber.kyc.exceptions;

import com.saber.kyc.dto.KycErrorResponse;

public class KycException extends RuntimeException {
    private KycErrorResponse errorResponse;

    public KycException(KycErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public KycException(String message, KycErrorResponse errorResponse) {
        super(message);
        this.errorResponse = errorResponse;
    }

    public KycException(String message, Throwable cause, KycErrorResponse errorResponse) {
        super(message, cause);
        this.errorResponse = errorResponse;
    }

    public KycException(Throwable cause, KycErrorResponse errorResponse) {
        super(cause);
        this.errorResponse = errorResponse;
    }

    public KycException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, KycErrorResponse errorResponse) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorResponse = errorResponse;
    }

    public KycErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
