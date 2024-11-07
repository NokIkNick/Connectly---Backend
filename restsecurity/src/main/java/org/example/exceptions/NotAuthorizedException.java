package org.example.exceptions;

import lombok.Getter;

@Getter
public class NotAuthorizedException extends Exception {
    private final int statusCode;

    public NotAuthorizedException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    public NotAuthorizedException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
