package com.bobmate.bobmate.exception;

public class TagNameDuplicateException extends RuntimeException{
    public TagNameDuplicateException() {
        super();
    }

    public TagNameDuplicateException(String message) {
        super(message);
    }

    public TagNameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNameDuplicateException(Throwable cause) {
        super(cause);
    }

    protected TagNameDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
