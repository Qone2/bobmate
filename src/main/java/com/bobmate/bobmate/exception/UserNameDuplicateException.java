package com.bobmate.bobmate.exception;

public class UserNameDuplicateException extends RuntimeException{
    public UserNameDuplicateException() {
        super();
    }

    public UserNameDuplicateException(String message) {
        super(message);
    }

    public UserNameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameDuplicateException(Throwable cause) {
        super(cause);
    }

    protected UserNameDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
