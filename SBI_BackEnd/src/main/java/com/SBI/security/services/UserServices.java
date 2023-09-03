package com.SBI.security.services;

import com.SBI.security.models.CompleteProfileDTO;
import com.SBI.security.models.User;
import com.SBI.security.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class UserServices implements UserDetailsService {

    public static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000L; //24 hours in milliseconds
    public static final int MAX_FAILED_ATTEMPTS = 3;

    private UserRepository userRepository;

    @Autowired
    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateResetPasswordToken(String token, String email, LocalDateTime expirationTime) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user + "bbbb");
            user.setResetPasswordToken(token);
            user.setExpirationTime(expirationTime);
            System.out.println(user + "abcd");
            userRepository.save(user); // Save the updated user back to the database
        }
        else {
            throw new UsernameNotFoundException("Impossible de trouver l'utilisateur avec l'e-mail: " + email);
        }
    }

    public User get(String resetPasswordToken) {
        System.out.println(resetPasswordToken + " R_P_Token");
        System.out.println(userRepository.findByResetPasswordToken(resetPasswordToken) + "AAAAA");
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

//    public void updateAccNonLocked(String email) {
//        userRepository.initializeAccNonLocked(email);
//    }

    public void increaseFailedAttempt(User user) {
        int newFailedAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempt(newFailedAttempts, user.getEmail());
    }

    public void lock(User user) {
        user.setAccNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    public boolean unlock(User user) {
        long lockTimeMilliS = user.getLockTime().getTime();
        long currentTimeInMilliS = System.currentTimeMillis();

        System.out.println(lockTimeMilliS + LOCK_TIME_DURATION);
        System.out.println(currentTimeInMilliS);
        System.out.println(lockTimeMilliS + LOCK_TIME_DURATION > currentTimeInMilliS);
        if (lockTimeMilliS + LOCK_TIME_DURATION < currentTimeInMilliS) {
            user.setAccNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempt(0, email);
    }

    /*@Override
    public MfaTokenData mfaSetup (String email) throws UsernameNotFoundException, QrGenerationException {

    }*/



    public CompleteProfileDTO getCompleteProfile(Integer UID){
        Optional<User> userOptional = userRepository.findById(UID);
        User user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + UID));
        return new CompleteProfileDTO(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return User.build(user);
    }
}
