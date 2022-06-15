package com.saber.kyc.exceptions;

public class GatewayException extends RuntimeException {
    public GatewayException() {
    }

    public GatewayException(String message) {
        super(message);
    }

    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public GatewayException(Throwable cause) {
        super(cause);
    }

    public GatewayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
