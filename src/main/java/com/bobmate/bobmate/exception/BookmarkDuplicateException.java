package com.bobmate.bobmate.exception;

public class BookmarkDuplicateException extends RuntimeException{
    public BookmarkDuplicateException() {
        super();
    }

    public BookmarkDuplicateException(String message) {
        super(message);
    }

    public BookmarkDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookmarkDuplicateException(Throwable cause) {
        super(cause);
    }

    protected BookmarkDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
