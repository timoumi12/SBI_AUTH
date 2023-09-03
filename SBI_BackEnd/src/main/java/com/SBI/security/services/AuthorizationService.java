package com.SBI.security.services;

import com.SBI.security.models.User;
import com.SBI.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    public boolean isAuthorized(Integer userId, String userEmail) {

        Optional<User> requestedUser = userRepository.findById(userId);
        Optional<User> requestingUser = userRepository.findByEmail(userEmail);

        if (requestedUser.isPresent() && requestingUser.isPresent()) {
            return requestingUser.get().getId().equals(requestedUser.get().getId());
        }
        return false;
    }
}