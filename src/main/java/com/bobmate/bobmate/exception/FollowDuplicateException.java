package com.bobmate.bobmate.exception;

public class FollowDuplicateException extends RuntimeException{
    public FollowDuplicateException() {
        super();
    }

    public FollowDuplicateException(String message) {
        super(message);
    }

    public FollowDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public FollowDuplicateException(Throwable cause) {
        super(cause);
    }

    protected FollowDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
