package com.SBI.security.controllers;

import com.SBI.security.auth.response.PasswordRecoveryResponse;
import com.SBI.security.auth.response.ResponseMessage;
import com.SBI.security.models.User;
import com.SBI.security.services.UserServices;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/password_recovery")
    public ResponseEntity<PasswordRecoveryResponse> processEmail(HttpServletRequest request) {

        String email = request.getParameter("email");
        String generatedToken = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        try {
            System.out.println(email + "####" + generatedToken + "####" + expirationTime);
            userServices.updateResetPasswordToken(generatedToken, email, expirationTime);

            //generate reset password link
            String resetPasswordLink = "http://localhost:4200/password_reset?token=" + generatedToken;
            //send email
            sendEmail(email, resetPasswordLink);

            PasswordRecoveryResponse response = new PasswordRecoveryResponse("Password recovery successful", generatedToken);
            System.out.println(response);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            System.out.println("Error updating reset password token: " + e.getMessage());
            return ResponseEntity.badRequest().body(new PasswordRecoveryResponse("Error updating reset password token", null));
        } catch (UnsupportedEncodingException | MessagingException e) {
            System.out.println("Error in the email sending process: " + e.getMessage());
            return ResponseEntity.badRequest().body(new PasswordRecoveryResponse("Error in the email sending process", null));
        }
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("emailtestingtool1.0@gmail.com", "SBI intern");
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello, </p>"
                + "<p>You have requested to reset ur password.</p>"
                + "<p>Click the link below:</p>"
                + "<p><b><a href= \"" + resetPasswordLink + "\">Change my password</a></b></p>"
                + "<p>Ignore this email if u have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

//    @GetMapping("/password_recovery")
//    public ResponseEntity<String> showResetPasswordForm(@Param(value = "token") String token) {
//
//        User user = userServices.get(token);
//        if (user != null) {
//            return ResponseEntity.ok("Password reset form goes here");
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Invalide ou expiré");
//        }
//    }

    @PostMapping("/process_password_recovery/{token}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> processResetPassword(@PathVariable String token, @RequestBody Map<String, String> requestBody) {

        String password = requestBody.get("password");

        System.out.println(token + "*********" + password);

        User user = userServices.get(token);
        System.out.println(user + "azerty");
        if (user != null) {
            LocalDateTime expirationTime = user.getExpirationTime();
            if (expirationTime == null || LocalDateTime.now().isAfter(expirationTime)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
            }
            System.out.println("test test");
            userServices.updatePassword(user, password);
            return ResponseEntity.ok(new ResponseMessage("You have successfully changed your password"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token Invalide ou expiré");
        }
    }
}
