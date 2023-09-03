/*
package com.SBI.security.security;

import com.SBI.security.models.User;
import com.SBI.security.user.UserRepository;
import com.SBI.security.services.UserServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;
    User user;
    @Autowired
    UserServices userServices;
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
                throws IOException, ServletException {

        String email = request.getParameter("email");
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.isAccNonLocked()) {
                if (user.getFailedAttempt() < UserServices.MAX_FAILED_ATTEMPTS - 1) {
                    userServices.increaseFailedAttempt(user);
                }
                else {
                    userServices.lock(user);
                    String errorMessage = "Your account has been locked due to 3 failed attempts."
                            + "\nIt will be unlocked after 24 hours.";
                    response.setStatus(HttpStatus.LOCKED.value());
                    response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
                    return;
                }
            }
            else if (!user.isAccNonLocked()) {
                if (userServices.unlock(user)) {
                    String Message = "Your account has been unlocked."
                            + "\nPlease try to login again.";
                    response.setStatus(HttpStatus.LOCKED.value());
                    response.getWriter().write("{\"error\": \"" + Message + "\"}");
                    return;
                }
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\": \"Invalid credentials\"}");
        }
        else {
            System.out.println("Email not exist");
            //throw new UsernameNotFoundException("Impossible de trouver l'utilisateur avec l'e-mail: " + email);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getWriter().write("{\"error\": \"Email not found\"}");
        }
        return;
    }
}
*/
