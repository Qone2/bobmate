package com.bobmate.bobmate.exception;

public class MeetMemberFullException extends RuntimeException{
    public MeetMemberFullException() {
        super();
    }

    public MeetMemberFullException(String message) {
        super(message);
    }

    public MeetMemberFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetMemberFullException(Throwable cause) {
        super(cause);
    }

    protected MeetMemberFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
