package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;
import java.util.Optional;

/**
 * The interface to realize report service of the system.
 */
public interface ReportService {

    static ReportService retrieve() {
        return AppReportService.getInstance();
    }

    /**
     * Returns all the {@link Report}s of the {@link Section} with the id equals {@param sectionId}
     * and the {@link Conference} with the id equals {@param conferenceId}.
     *
     * @param sectionId    is the id of the {@link Section} which {@link Report}s are to return.
     * @param conferenceId is the id of the {@link Conference} which {@link Report}s are to return.
     * @return all the {@link Report}s of the {@link Section} with the id equals {@param sectionId}
     * and the {@link Conference} with the id equals {@param conferenceId}.
     */
    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);

    /**
     * Returns {@link Optional<Report>} that contains {@link Report} with the id equals {@param id} and null
     * if there is no {@link Report} with the id equals {@param id} in the system.
     *
     * @param id is the value of the id of the {@link Report} to find.
     * @return {@link Optional<Report>} that contains {@link Report} with the id equals {@param id} and null
     * if there is no {@link Report} with the id equals {@param id} in the system.
     */
    Optional<Report> findReportByID(Long id);

    /**
     * Updates the {@link Report}.
     *
     * @param reportToUpdate is the {@link Report} to update.
     */
    void updateReport(Report reportToUpdate);

    /**
     * Creates a new {@link Report} in the system.
     *
     * @param reportToCreate is the {@link Report} to create.
     * @throws DuplicateException if there is the {@link Section}
     *                            with the sectionName of the {@param reportToCreate}
     *                            and there is a violation of unique constrains in the database.
     */
    void createReport(Report reportToCreate) throws DuplicateException;

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.QUESTION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    List<Report> findAllQuestions(Long managerId);

    /**
     * Finds all the {@link Report}s in the system that have the parameter questionReportId equals to the value
     * of the {@param questionReportId}
     * @param questionReportId is the {@link Long} value of the parameter questionReportId of the Report.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the value of the parameter
     * questionReportId equals to the {@param questionReportId}.
     */
    List<Report> findAllReportsByQuestionId(Long questionReportId);

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which were created by a {@link User} with the id equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} questions created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.QUESTION,
     * which are created by the {@link User} with id equals {@param managerId}.
     */
    List<Report> findApplicantQuestions(Long managerId);

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.APPLICATION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    List<Report> findAllApplications(Long managerId);

    /**
     * Finds all {@link Report}s in the system that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which were created by a {@link User} with the id equals {@param applicantId}.
     *
     * @param applicantId is the {@link Long} that is id of the {@link User} application created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the ReportType.APPLICATION,
     * which are created by the {@link User} with id equals {@param applicantId}.
     */
    List<Report> findApplicantApplications(Long applicantId);
}