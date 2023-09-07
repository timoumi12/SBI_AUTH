package com.SBI.security.controllers;

import com.SBI.security.auth.request.AuthenticationRequest;
import com.SBI.security.auth.response.AuthenticationResponse;
import com.SBI.security.auth.request.RegisterRequest;
import com.SBI.security.auth.response.ResponseMessage;
import com.SBI.security.config.jwt.JwtUtils;
import com.SBI.security.models.Role;
import com.SBI.security.models.User;
import com.SBI.security.services.UserServices;
import com.SBI.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServices userServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        Role userRole;
        boolean is_acc_non_locked;

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResponseMessage("Error: Email is already in use!"));
        }

        if (request.getRole() == null) {
            userRole = Role.USER;
        } else {
            userRole = request.getRole();
        }
        // Check if the provided role is a valid enum value
        if (!isValidRole(userRole)) {
            throw new IllegalArgumentException("Invalid role: " + userRole);
        }

        // Create new user's account
        User user = new User(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                userRole,
                true);

        System.out.println(user);
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
    }

    private boolean isValidRole(Role role) {
        for (Role validRole : Role.values()) {
            if (validRole == role) {
                return true;
            }
        }
        return false;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            if (!user.isAccNonLocked()) {
                if (userServices.unlock(user)) {
                    String Message = "Your account has been unlocked."
                            + " Please try to login again.";
                    return ResponseEntity.status(HttpStatus.LOCKED.value()).body(new ResponseMessage(Message));
                }
                String message = "Your account is locked due to too many failed attempts. It will be unlocked after 24 hours.";
                return ResponseEntity.status(HttpStatus.LOCKED).body(new ResponseMessage(message));
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            String roleName = user.getAuthorities().iterator().next().getAuthority();

            if (user.getFailedAttempt() > 0) {
                userServices.resetFailedAttempts(user.getEmail());
            }

            return ResponseEntity.ok(new AuthenticationResponse(jwt,
                    user.getId(),
                    user.getEmail(),
                    user.getFirstname(),
                    user.getLastname(),
                    roleName)
            );
        } catch (BadCredentialsException e) {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getFailedAttempt() < UserServices.MAX_FAILED_ATTEMPTS - 1) {
                    userServices.increaseFailedAttempt(user);
                } else {
                    userServices.lock(user);
                    String errorMessage = "Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.";
                    return ResponseEntity.status(HttpStatus.LOCKED.value())
                            .body(new ResponseMessage(errorMessage));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found"));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessage("Invalid credentials"));
        } /*catch (UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage("User not found"));
        }*/
    }
}
