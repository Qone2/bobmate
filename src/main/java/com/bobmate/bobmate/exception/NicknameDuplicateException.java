package com.bobmate.bobmate.exception;

public class NicknameDuplicateException extends RuntimeException{
    public NicknameDuplicateException() {
        super();
    }

    public NicknameDuplicateException(String message) {
        super(message);
    }

    public NicknameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public NicknameDuplicateException(Throwable cause) {
        super(cause);
    }

    protected NicknameDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
