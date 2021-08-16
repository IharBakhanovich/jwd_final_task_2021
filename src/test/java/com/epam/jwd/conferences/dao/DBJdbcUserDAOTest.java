package com.epam.jwd.conferences.dao;

import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.UserDAO;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;

/**
 * Test for the DBJdbcUserDAO
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBJdbcUserDAOTest {
    private final UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    private List<User> users;
    private final BCrypt.Hasher hasher = BCrypt.withDefaults();
    User user = new User("testUser", "password");
    final char[] rawPassword = user.getPassword().toCharArray();
    final String encryptedPassword = hasher.hashToString(MIN_COST, rawPassword);
    private Long userId;
    String newUserNickname;

    @BeforeAll
    public void setUp() {
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
        User userToSave
                = new User(user.getEmail(), encryptedPassword,
                user.getSalt(), user.getNumberLoginAttempts(), user.getVerificationToken(),
                user.isEmailVerified(), user.getNickname(), user.getFirstName(),
                user.getSurname(), user.getRole());
        try {
            userDAO.save(userToSave);
            users = userDAO.findAll();
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        for (User userInLoop : users
        ) {
            if (userInLoop.getNickname().equals(user.getNickname())) {
                userId = userInLoop.getId();
            }
        }
    }

    @Test
    @Order(1)
    void InsertUser() {
        Long userIdFromDatabase = null;

        for (User userInLoop : users
        ) {
            if (userInLoop.getNickname().equals(user.getNickname())) {
                userIdFromDatabase = userInLoop.getId();
            }
        }
        Assertions.assertEquals(userIdFromDatabase, userId);
    }

    @Test
    @Order(2)
    void InsertConferenceDuplicate() {
        Assertions.assertThrows(DuplicateException.class, () -> userDAO.save(user));
    }

    @Test
    @Order(3)
    void findUser() {
        Optional<User> userFromDatabase = userDAO.findById(userId);
        if (userFromDatabase.isPresent()) {
            Assertions.assertEquals(userFromDatabase.get().getNickname(), user.getNickname());
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(4)
    void UpdateUser() {
        String newUserNickname = "TestsPassedUser";
        Optional<User> userFromDatabase = userDAO.findById(userId);
        User updatedUser = null;
        if (userFromDatabase.isPresent()) {
            updatedUser = new User(userId, userFromDatabase.get().getEmail(),
                    userFromDatabase.get().getPassword(), userFromDatabase.get().getSalt(),
                    userFromDatabase.get().getNumberLoginAttempts(), userFromDatabase.get().getVerificationToken(),
                    userFromDatabase.get().isEmailVerified(), newUserNickname,
                    userFromDatabase.get().getFirstName(), userFromDatabase.get().getSurname(),
                    userFromDatabase.get().getRole());
        }
        userDAO.update(updatedUser);

        Optional<User> userFromDatabaseAfterUpdate = userDAO.findById(userId);
        userFromDatabaseAfterUpdate.ifPresent(value -> this.newUserNickname = value.getNickname());

        if (userFromDatabaseAfterUpdate.isPresent() && userFromDatabase.isPresent()) {
            Assertions.assertEquals(userFromDatabaseAfterUpdate.get().getNickname(),
                    updatedUser.getNickname());
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(5)
    void DeleteUser() {
        Optional<User> userFromDatabase = userDAO.findById(userId);
        if (userFromDatabase.isPresent()) {
            if (userFromDatabase.get().getNickname().equals(newUserNickname)) {
                userDAO.delete(userId);
                Optional<User> foundedUser = userDAO.findById(userId);
                Assertions.assertFalse(foundedUser.isPresent());
            } else {
                Assertions.fail();
            }
        } else {
            Assertions.fail();
        }
    }


    @AfterAll
    public void tearDown() {
        users.clear();
        ConnectionPool.retrieve().destroy();
    }
}
