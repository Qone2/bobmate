package com.bobmate.bobmate.exception;

public class DeletedReviewException extends RuntimeException{
    public DeletedReviewException() {
        super();
    }

    public DeletedReviewException(String message) {
        super(message);
    }

    public DeletedReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedReviewException(Throwable cause) {
        super(cause);
    }

    protected DeletedReviewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
