package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dao.impl.DBConferenceDAO;
import com.epam.jwd.Conferences.dto.Conference;

import java.util.List;
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

    /**
     * Finds all {@link Conference}s in the database with the parameter managerConf equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Conference}s parameter managerConf to find.
     * @return {@link List<Conference>} that contains all the {@link Conference}s in the database with the parameter
     * managerConf equals {@param userId}.
     */
    List<Conference> findAllConferencesWhereUserIsManager(Long userId);
}