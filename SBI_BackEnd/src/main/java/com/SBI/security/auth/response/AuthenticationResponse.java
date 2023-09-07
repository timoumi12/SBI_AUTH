package com.SBI.security.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String roleName;
    private String  firstname;
    private String lastname;

    public AuthenticationResponse(String accessToken, Long id, String email, String firstname, String lastname, String roleName) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roleName = roleName;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}