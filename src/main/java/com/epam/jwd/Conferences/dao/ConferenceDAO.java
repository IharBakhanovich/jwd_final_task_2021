package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Conference;

import java.util.Optional;

/**
 * The interface of the DAO for the table 'conferences'.
 */
public interface ConferenceDAO extends DAO<Conference,Long> {

    Optional<Conference> findConferenceByTitle(String title);

    /**
     * Returns the implementation of the ConferenceDAO.
     *
     * @return Object that is the implementation of the ConferenceDAO.
     */
    static ConferenceDAO retrieve() {
        return DBConferenceDAO.getInstance();
    }
}
