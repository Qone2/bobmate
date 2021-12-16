package com.bobmate.bobmate.exception;

public class HeadMemberException extends RuntimeException{
    public HeadMemberException() {
        super();
    }

    public HeadMemberException(String message) {
        super(message);
    }

    public HeadMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeadMemberException(Throwable cause) {
        super(cause);
    }

    protected HeadMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
