package com.personasphere.auth_service.exceptions;

public class AuthUserNotFoundException extends RuntimeException {
    public AuthUserNotFoundException(String message) {
        super(message);
    }
}
