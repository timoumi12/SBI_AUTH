package com.SBI.security.auth.response;

public class PasswordRecoveryResponse {
    private String message;
    private String recoveryToken;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecoveryToken(String recoveryToken) {
        this.recoveryToken = recoveryToken;
    }


    public String getMessage() {
        return message;
    }

    public String getRecoveryToken() {
        return recoveryToken;
    }

    public PasswordRecoveryResponse(String message, String recoveryToken) {
        this.message = message;
        this.recoveryToken = recoveryToken;
    }
}
