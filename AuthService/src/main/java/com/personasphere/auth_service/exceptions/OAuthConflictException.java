package com.personasphere.auth_service.exceptions;

public class OAuthConflictException extends RuntimeException {
    public OAuthConflictException(String message) {
        super(message);
    }
}
