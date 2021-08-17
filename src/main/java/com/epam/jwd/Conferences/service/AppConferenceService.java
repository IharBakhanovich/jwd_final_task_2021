package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;

/**
 * This is a service (model) of the application. The singleton.
 */
public class AppConferenceService implements ConferenceService {

    private final ConferenceDAO conferenceDAO;

    private AppConferenceService() {
        this.conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    }

    private static class AppConferenceServiceHolder {
        private final static AppConferenceService instance
                = new AppConferenceService();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppConferenceService getInstance() {
        return AppConferenceService.AppConferenceServiceHolder.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Conference> findAllConferences() {
        return conferenceDAO.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createConference(Conference conferenceToCreate) throws DuplicateException {
        conferenceDAO.save(conferenceToCreate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConference(Conference conferenceToUpdate) {
        conferenceDAO.update(conferenceToUpdate);
    }
}
