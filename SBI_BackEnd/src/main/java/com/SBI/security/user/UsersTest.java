package com.SBI.security.user;

import com.SBI.security.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UsersTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testUpdateFailedAttempt() {
        String email = "ihebtimoumi12@gmail.com";
        int failedAttempt = 2;
        userRepository.updateFailedAttempt(failedAttempt, email);
        Integer userId = 1;
        User user = entityManager.find(User.class, userId);
        assertEquals(failedAttempt, user.getFailedAttempt());
    }
}
