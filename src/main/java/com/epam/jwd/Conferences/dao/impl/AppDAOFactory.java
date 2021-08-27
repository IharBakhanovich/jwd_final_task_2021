package com.epam.jwd.Conferences.dao.impl;

import com.epam.jwd.Conferences.dao.*;

/**
 * This is a factory for the DAOs. The singleton.
 */
public class AppDAOFactory implements DAOFactory {

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private AppDAOFactory() {

    }

    private static class AppDAOFactoryHolder {
        private final static AppDAOFactory instance
                = new AppDAOFactory();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppDAOFactory getInstance() {
        return AppDAOFactory.AppDAOFactoryHolder.instance;
    }

    /**
     * Returns the instance of the UserDAO class.
     *
     * @return Object of the UserDAO class.
     */
    @Override
    public UserDAO getUserDAO() {
        return UserDAO.retrieve();
    }

    /**
     * Returns the instance of the ConferenceDAO class.
     *
     * @return Object of the ConferenceDAO class.
     */
    @Override
    public ConferenceDAO getConferenceDAO() {
        return ConferenceDAO.retrieve();
    }

    /**
     * Returns the instance of the ReportDAO class.
     *
     * @return Object of the ReportDAO class.
     */
    @Override
    public ReportDAO getReportDAO() {
        return ReportDAO.retrieve();
    }

    /**
     * Returns the instance of the SectionDAO class.
     *
     * @return Object of the SectionDAO class.
     */
    @Override
    public SectionDAO getSectionDAO() {
        return SectionDAO.retrieve();
    }
}
