package com.epam.jwd.Conferences.dao;

/**
 * This is a factory for the DAOs. The singleton.
 */
public class AppDAOFactory implements DAOFactory {

    AppDAOFactory() {

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

    @Override
    public UserDAO getUserDAO() {
        return UserDAO.retrieve();
    }

    @Override
    public ConferenceDAO getConferenceDAO() {
        return ConferenceDAO.retrieve();
    }

    @Override
    public ReportDAO getReportDAO() {
        return ReportDAO.retrieve();
    }

    @Override
    public SectionDAO getSectionDAO() {
        return SectionDAO.retrieve();
    }
}
