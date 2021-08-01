package com.epam.jwd.Conferences.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.ReportDAO;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;
import java.util.Optional;

/**
 * This is a service (model) of the application. The singleton.
 */
public class AppReportService implements ReportService {

    private final ReportDAO reportDAO;

    private AppReportService() {
        this.reportDAO = DAOFactory.getInstance().getReportDAO();
    }

    private static class AppReportServiceHolder {
        private final static AppReportService instance
                = new AppReportService();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppReportService getInstance() {
        return AppReportService.AppReportServiceHolder.instance;
    }


    @Override
    public List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId) {
        return reportDAO.findAllReportsBySectionID(sectionId, conferenceId);
    }

    @Override
    public Optional<Report> findReportByID(Long id) {
        return reportDAO.findById(id);
    }

    @Override
    public void updateReport(Report reportToUpdate) {
        reportDAO.update(reportToUpdate);
    }

    @Override
    public void createReport(Report reportToCreate) throws DuplicateException {
        reportDAO.save(reportToCreate);
    }
}
