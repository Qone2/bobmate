package com.bobmate.bobmate.exception;

public class DeletedPlaceException extends RuntimeException{
    public DeletedPlaceException() {
        super();
    }

    public DeletedPlaceException(String message) {
        super(message);
    }

    public DeletedPlaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedPlaceException(Throwable cause) {
        super(cause);
    }

    protected DeletedPlaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
