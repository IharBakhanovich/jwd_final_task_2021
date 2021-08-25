package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dao.impl.JdbcUserDAO;
import com.epam.jwd.Conferences.dto.User;

import java.util.Optional;

/**
 * The interface of the DAO for the table 'users'.
 */
public interface UserDAO extends DAO<User, Long> {

    /**
     * Finds {@link Optional<User>} by the parameter nickname of {@link User}, which has the value
     * equals to the {@param nickname}.
     *
     * @param nickname is the {@link String} which is the nickname to search.
     * @return {@link Optional<User>} by the parameter nickname of {@link User}, which has the value
     * equals to the {@param nickname}.
     */
    Optional<User> findUserByNickname(String nickname);

    /**
     * Returns the implementation of the UserDAO.
     *
     * @return Object that is the implementation of the ConferenceDAO.
     */
    static UserDAO retrieve() {
        return JdbcUserDAO.getInstance();
    }

    /**
     * Updates {@link User}s parameter role.
     *
     * @param userId  is the id of the {@link User} which role is to update.
     * @param newRole is the value of the new role of the {@link User} with the id equals {@param userId}.
     */
    void updateUserRoleByUserId(Long userId, Long newRole);
}