package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;

public interface ConferenceService {

    static ConferenceService retrieve() {
        return AppConferenceService.getInstance();
    }

    List<Conference> findAllConferences();

    void createConference(Conference conferenceToCreate) throws DuplicateException;

    void updateConference(Conference conferenceToUpdate);

}
