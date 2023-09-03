package com.SBI.security.auth;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends RuntimeException {

    public CustomAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}