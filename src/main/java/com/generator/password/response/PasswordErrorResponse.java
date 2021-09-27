package com.generator.password.response;

public class PasswordErrorResponse {

    private final int status;
    private final String message;
    private final long timeStamp;

    public PasswordErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
