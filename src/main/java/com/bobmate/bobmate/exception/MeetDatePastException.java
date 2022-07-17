package com.bobmate.bobmate.exception;

public class MeetDatePastException extends RuntimeException{
    public MeetDatePastException() {
        super();
    }

    public MeetDatePastException(String message) {
        super(message);
    }

    public MeetDatePastException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetDatePastException(Throwable cause) {
        super(cause);
    }

    protected MeetDatePastException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
