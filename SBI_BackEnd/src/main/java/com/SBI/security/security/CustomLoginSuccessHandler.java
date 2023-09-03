/*
package com.SBI.security.security;

import com.SBI.security.models.User;
import com.SBI.security.services.UserServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserServices userServices;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication)
                throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        if (user.getFailedAttempt() > 0) {
            userServices.resetFailedAttempts(user.getEmail());
        }
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
*/
