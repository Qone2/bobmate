package com.bobmate.bobmate.exception;

public class LikeDuplicateException extends RuntimeException{
    public LikeDuplicateException() {
        super();
    }

    public LikeDuplicateException(String message) {
        super(message);
    }

    public LikeDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LikeDuplicateException(Throwable cause) {
        super(cause);
    }

    protected LikeDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
