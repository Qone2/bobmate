package com.bobmate.bobmate.exception;

public class MemberMeetDuplicateException extends RuntimeException{
    public MemberMeetDuplicateException() {
        super();
    }

    public MemberMeetDuplicateException(String message) {
        super(message);
    }

    public MemberMeetDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberMeetDuplicateException(Throwable cause) {
        super(cause);
    }

    protected MemberMeetDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
