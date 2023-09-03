package com.SBI.security.controllers;

import com.SBI.security.models.CompleteProfileDTO;
import com.SBI.security.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/profile")
public class ProfileController {

    private final UserServices userServices;

    public ProfileController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CompleteProfileDTO> getCompleteProfile(@PathVariable Integer userId) {
        try {
            CompleteProfileDTO completeProfile = userServices.getCompleteProfile(userId);
            return ResponseEntity.ok(completeProfile);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
        }
    }
}
