package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.User;

import java.util.List;
import java.util.Optional;

public interface ReportDAO extends DAO<Report, Long> {

    Optional<Report> findReportByApplicantId(Long applicantId);

    /**
     * Returns the implementation of the reportDAO.
     *
     * @return Object that is the implementation of the ReportDAO.
     */
    static ReportDAO retrieve() {
        return DBReportDAO.getInstance();
    }

    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);

    List<Report> findAllQuestionsByManagerId(Long managerId);

    List<Report> findAllReportsByQuestionReportId(Long questionReportId);

    List<Report> findAllQuestionsByApplicantId(Long managerId);
}
