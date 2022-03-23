package com.bobmate.bobmate.exception;

public class MemberEmailException extends RuntimeException{
    public MemberEmailException() {
        super();
    }

    public MemberEmailException(String message) {
        super(message);
    }

    public MemberEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberEmailException(Throwable cause) {
        super(cause);
    }

    protected MemberEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
