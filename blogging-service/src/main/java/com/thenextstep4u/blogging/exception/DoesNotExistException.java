package com.thenextstep4u.blogging.exception;

public class DoesNotExistException extends Exception {


    public DoesNotExistException() {
    }

    public DoesNotExistException(String message) {
        super(message);
    }

    public DoesNotExistException(Throwable cause) {
        super(cause);
    }

    public DoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoesNotExistException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
