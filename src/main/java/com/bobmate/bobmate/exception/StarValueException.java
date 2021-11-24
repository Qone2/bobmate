package com.bobmate.bobmate.exception;

public class StarValueException extends RuntimeException{
    public StarValueException() {
    }

    public StarValueException(String message) {
        super(message);
    }

    public StarValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public StarValueException(Throwable cause) {
        super(cause);
    }

    public StarValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
