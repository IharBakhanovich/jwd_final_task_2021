package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dao.impl.AppDAOFactory;

public interface DAOFactory {

    /**
     * Returns the instance of the UserDAO class.
     *
     * @return Object of the UserDAO class.
     */
    UserDAO getUserDAO();

    /**
     * Returns the instance of the ConferenceDAO class.
     *
     * @return Object of the ConferenceDAO class.
     */
    ConferenceDAO getConferenceDAO();

    /**
     * Returns the instance of the ReportDAO class.
     *
     * @return Object of the ReportDAO class.
     */
    ReportDAO getReportDAO();

    /**
     * Returns the instance of the SectionDAO class.
     *
     * @return Object of the SectionDAO class.
     */
    SectionDAO getSectionDAO();

    static DAOFactory getInstance(){
        return AppDAOFactory.getInstance();
    }
}