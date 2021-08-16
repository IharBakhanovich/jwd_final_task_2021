package com.epam.jwd.conferences.service;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dao.UserDAO;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Test for the DBSectionDAO
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTest {
    String nickNameUserForTest = "testUser";
    String passwordUserForTest = "password";
    User userForTest = new User(nickNameUserForTest, passwordUserForTest);
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    Long idUserForTest;
    UserService service = UserService.retrieve();
    String updatedFirstName = "Valentino";
    String updatedSurname = "Rossi";
    List<User> allUserInDatabase;

    @BeforeAll
    public void setUp() {
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
        allUserInDatabase = service.findAllUsers();
    }

    @Test
    @Order(1)
    void createUser() {

        try {
            service.createUser(userForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        Optional<User> userFromDatabase = userDAO.findUserByNickname(nickNameUserForTest);
        if (userFromDatabase.isPresent()) {
            idUserForTest = userFromDatabase.get().getId();
            Assertions.assertEquals(userFromDatabase.get().getNickname(), nickNameUserForTest);
        }
    }

    @Test
    @Order(2)
    void canUserLogin() {
        Assertions.assertTrue(service.canLogIn(userForTest));
    }

    @Test
    @Order(3)
    void userFindByLogin() {
        User userFromDatabase = service.findByLogin(nickNameUserForTest);
        allUserInDatabase.add(userFromDatabase);
        Assertions.assertEquals(userFromDatabase.getNickname(), nickNameUserForTest);
    }

    @Test
    @Order(4)
    void findUserById() {
        Optional<User> userFromDatabase = service.findUserByID(idUserForTest);
        if (userFromDatabase.isPresent()) {
            Assertions.assertEquals(userFromDatabase.get().getNickname(), nickNameUserForTest);
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(5)
    void updateUser() {
        User userFromDatabase = service.findByLogin(nickNameUserForTest);
        User updatedUser = new User(idUserForTest, userFromDatabase.getEmail(), userFromDatabase.getPassword(),
                userFromDatabase.getSalt(), userFromDatabase.getNumberLoginAttempts(),
                userFromDatabase.getVerificationToken(), userFromDatabase.isEmailVerified(), nickNameUserForTest,
                updatedFirstName, updatedSurname, userFromDatabase.getRole());
        service.updateUser(updatedUser);

        Optional<User> userFromDatabaseAfterUpdate = service.findUserByID(idUserForTest);
        if (userFromDatabaseAfterUpdate.isPresent()) {
            Assertions.assertTrue(
                    userFromDatabaseAfterUpdate.get().getFirstName().equals(updatedFirstName)
                            && userFromDatabaseAfterUpdate.get().getSurname().equals(updatedSurname)
            );
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(6)
    void updateUserRole() {

        Long updatedRole = Role.MANAGER.getId();

        service.updateUserRole(idUserForTest, updatedRole);

        Optional<User> userFromDatabaseAfterUpdateRole = service.findUserByID(idUserForTest);
        if (userFromDatabaseAfterUpdateRole.isPresent()) {
            Assertions.assertEquals(userFromDatabaseAfterUpdateRole.get().getRole(),
                    Role.resolveRoleById(updatedRole));
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(7)
    void findAllUser() {

        List<User> allUsersInDatabaseAfterUpdate = service.findAllUsers();
        List<String> allNicknamesInDatabaseAfterUpdate = new ArrayList<>();
        List<String> allNicknamesInDatabase = new ArrayList<>();
        for (User user : allUsersInDatabaseAfterUpdate
        ) {
           allNicknamesInDatabaseAfterUpdate.add(user.getNickname());
        }
        for (User user: allUserInDatabase
             ) {
            allNicknamesInDatabase.add(user.getNickname());
        }
        for (String nickname: allNicknamesInDatabase
             ) {
            if (!allNicknamesInDatabaseAfterUpdate.contains(nickname)) {
                Assertions.fail();
            }
        }
        Assertions.assertTrue(true);
    }

    @AfterAll
    public void tearDown() {
        userDAO.delete(idUserForTest);
        allUserInDatabase.clear();
        ConnectionPool.retrieve().destroy();
    }
}