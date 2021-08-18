package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.User;

import java.util.List;
import java.util.Optional;

public interface ReportDAO extends DAO<Report, Long> {

    /**
     * Finds the {@link Report} of the applicant.
     *
     * @param applicantId The Id of the applicant of the returned {@link Report} report.
     * @return {@link Report}.
     */
    Optional<Report> findReportByApplicantId(Long applicantId);

    /**
     * Returns the implementation of the reportDAO.
     *
     * @return Object that is the implementation of the ReportDAO.
     */
    static ReportDAO retrieve() {
        return DBReportDAO.getInstance();
    }

    /**
     * Finds all {@link Report}s in the database by sectionId and conferenceId.
     *
     * @param sectionId is the {@link Long} fo the sectionId to find.
     * @param conferenceId is the {@link Long} fo the conferenceId to find.
     * @return {@link List<Report>} that contains all the reports
     *         in the {@param conferenceId}/{@param sectionId} section
     */
    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.QUESTION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    List<Report> findAllQuestionsByManagerId(Long managerId);

    /**
     * Finds all the {@link Report}s in the database that have the parameter questionReportId equals to the value
     * of the {@param questionReportId}
     * @param questionReportId is the {@link Long} value of the parameter questionReportId of the Report.
     * @return {@link List<Report>} that contains all the {@link Report}s with the value of the parameter
     * questionReportId equals to the {@param questionReportId}.
     */
    List<Report> findAllReportsByQuestionReportId(Long questionReportId);

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which were created by a {@link User} with the id equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} questions created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.QUESTION,
     * which are created by the {@link User} with id equals {@param managerId}.
     */
    List<Report> findAllQuestionsByApplicantId(Long managerId);

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.APPLICATION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    List<Report> findAllApplicationsByManagerId(Long managerId);

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which were created by a {@link User} with the id equals {@param applicantId}.
     *
     * @param applicantId is the {@link Long} that is id of the {@link User} application created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.APPLICATION,
     * which are created by the {@link User} with id equals {@param applicantId}.
     */
    List<Report> findAllApplicationsByApplicantId(Long applicantId);

    /**
     * Finds all {@link Report}s in the database that have the parameter applicant equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Report}s parameter applicant to find.
     * @return {@link List<Report>} that contains all the {@link Report}s in the database with the parameter applicant
     * equals {@param userId}.
     */
    List<Report> findAllReportsByUserId(Long userId);
}
