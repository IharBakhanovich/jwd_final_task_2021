package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;

/**
 * The interface to realize conference service of the system.
 */
public interface ConferenceService {

    static ConferenceService retrieve() {
        return AppConferenceService.getInstance();
    }

    /**
     * Returns all {@link Conference}es in the system.
     *
     * @return {@link List<Conference>} that contains all conferences in the system.
     */
    List<Conference> findAllConferences();

    /**
     * Creates a new {@link Conference} in the system.
     *
     * @param conferenceToCreate is the {@link Conference} to create.
     * @throws DuplicateException if there is the {@link Conference}
     *                            with the conferenceTitle of the {@param conferenceToCreate}
     */
    void createConference(Conference conferenceToCreate) throws DuplicateException;

    /**
     * Updates the {@link Conference}.
     *
     * @param conferenceToUpdate is the {@link Conference} to update.
     */
    void updateConference(Conference conferenceToUpdate);

}
