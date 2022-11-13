package org.rtcal.lib.exceptions;

public class EventException extends Exception {

    private final Throwable cause;

    public EventException(Throwable cause) {
        this.cause = cause;
    }

    public EventException() {
        this.cause = null;
    }

    public EventException(Throwable cause, String message) {
        super(message);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
