package com.bobmate.bobmate.exception;

public class WrongIdPasswordException extends RuntimeException{
    public WrongIdPasswordException() {
        super();
    }

    public WrongIdPasswordException(String message) {
        super(message);
    }

    public WrongIdPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongIdPasswordException(Throwable cause) {
        super(cause);
    }

    protected WrongIdPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
