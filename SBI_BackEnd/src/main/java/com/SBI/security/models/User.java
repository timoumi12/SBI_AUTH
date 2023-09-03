package com.SBI.security.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String resetPasswordToken;
    @Column(name = "token_expiration_time")
    private LocalDateTime expirationTime;
    @Column(name = "account_non_locked")
    private boolean accNonLocked;
    @Column(name = "two_fa_enabled")
    private boolean TwoFA_Enabled;
    @Column(name = "two_fa_key")
    private String twoFA_Key;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "failed_attempt")
    private int failedAttempt;
    @Column(name = "lock_time")
    private Date lockTime;

    public User(Long id,
                String firstname,
                String lastname,
                String email,
                String password,
                Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String firstname,
                String lastname,
                String email,
                String encode,
                Role role,
                boolean accNonLocked) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = encode;
        this.role = role;
        this.accNonLocked = accNonLocked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAccNonLocked() {
        return accNonLocked;
    }

    public void setAccNonLocked(boolean accNonLocked) {
        this.accNonLocked = accNonLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }


    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singleton(authority);
    }

    public static User build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        }
        return new User(
                user.getId(),
                user.getLastname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
