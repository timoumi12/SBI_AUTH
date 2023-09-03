package com.SBI.security.user;

import com.SBI.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findById(Integer Id);

    User findByResetPasswordToken(String token);

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    @Modifying
    public void updateFailedAttempt(int failedAttempt, String email);

    /*@Query("UPDATE User u SET u.accNonLocked = true WHERE u.email = ?1")
    @Modifying
    public void initializeAccNonLocked(String email);*/
}
