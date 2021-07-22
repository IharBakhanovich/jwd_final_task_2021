package com.epam.jwd.Conferences.dao;

public interface DAOFactory {
    UserDAO getUserDAO();

    ConferenceDAO getConferenceDAO();

    ReportDAO getReportDAO();

    SectionDAO getSectionDAO();

    static DAOFactory getInstance(){
        return AppDAOFactory.getInstance();
    }
}
