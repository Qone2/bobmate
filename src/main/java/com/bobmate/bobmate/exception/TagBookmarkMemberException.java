package com.bobmate.bobmate.exception;

public class TagBookmarkMemberException extends RuntimeException{
    public TagBookmarkMemberException() {
        super();
    }

    public TagBookmarkMemberException(String message) {
        super(message);
    }

    public TagBookmarkMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagBookmarkMemberException(Throwable cause) {
        super(cause);
    }

    protected TagBookmarkMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
