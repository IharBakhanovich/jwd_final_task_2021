package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Conference;

import java.util.Optional;

/**
 * The interface of the DAO for the table 'conferences'.
 */
public interface ConferenceDAO extends DAO<Conference,Long> {

    /**
     * Finds {@link Optional<Conference>} by the parameter conferenceTitle of the {@link Conference}.
     *
     * @param title is a String, that is a title of the Conference.
     * @return {@link Optional<Conference>}, that contains the {@link Conference} if it is exist in the system.
     */
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
