package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.User;

import java.util.Optional;

/**
 * The interface of the DAO for the table 'users'.
 */
public interface UserDAO extends DAO<User, Long> {

    Optional<User> findUserByNickname(String nickname);

    /**
     * Returns the implementation of the UserDAO.
     *
     * @return Object that is the implementation of the ConferenceDAO.
     */
    static UserDAO retrieve() {
        return JdbcUserDAO.getInstance();
    }

}
