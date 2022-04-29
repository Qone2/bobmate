package com.bobmate.bobmate.exception;

public class DeletedMemberException extends RuntimeException{
    public DeletedMemberException() {
        super();
    }

    public DeletedMemberException(String message) {
        super(message);
    }

    public DeletedMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedMemberException(Throwable cause) {
        super(cause);
    }

    protected DeletedMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
