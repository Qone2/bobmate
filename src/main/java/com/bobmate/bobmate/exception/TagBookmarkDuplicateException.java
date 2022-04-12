package com.bobmate.bobmate.exception;

public class TagBookmarkDuplicateException extends RuntimeException{
    public TagBookmarkDuplicateException() {
        super();
    }

    public TagBookmarkDuplicateException(String message) {
        super(message);
    }

    public TagBookmarkDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagBookmarkDuplicateException(Throwable cause) {
        super(cause);
    }

    protected TagBookmarkDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
