package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;
import java.util.Optional;

/**
 * An interface to realise the service layer of the system.
 */
public interface UserService {

    static UserService retrieve() {
        return AppUserService.getInstance();
    }

    /**
     * Creates a new user in the system.
     *
     * @param user is the user to create.
     * @throws DuplicateException if there is the user with the such a nickname in the system.
     */
    void createUser(User user) throws DuplicateException;

    /**
     * Checks if the user can login in the system.
     *
     * @param user is the {@link User} to login in the system.
     * @return true if the {@param user} can login.
     */
    boolean canLogIn(User user);

    /**
     * Finds {@link User} by its nickname.
     *
     * @param login is the nickname of the user to find.
     * @return {@link User}
     */
    User findByLogin(String login);

    /**
     * Returns all {@link User}s in the system.
     *
     * @return {@link List<User>} that contains all users in the system.
     */
    List<User> findAllUsers();

    void clean();

    /**
     * Returns all {@link Conference}es in the system.
     *
     * @return {@link List<Conference>} that contains all conferences in the system.
     */
    List<Conference> findAllConferences();

    /**
     * Returns all the {@link Section}s of the {@link Conference} with the id equals {@param id}.
     *
     * @param id is the id of the {@link Conference} which {@link Section}s are to return.
     * @return all the {@link Section}s of the {@link Conference} with the id equals {@param id}.
     */
    List<Section> findAllSectionsByConferenceID(Long id);

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
     * Returns {@link Optional<User>} that contains {@link User} with the id equals {@param id} and null
     * if there is no {@link User} with the id equals {@param id} in the system.
     *
     * @param id is the value of the id of the {@link User} to find.
     * @return {@link Optional<User>} that contains {@link User} with the id equals {@param id} and null
     * if there is no {@link User} with the id equals {@param id} in the system.
     */
    Optional<User> findUserByID(Long id);

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
     * Returns all {@link Section}s in the system.
     *
     * @return {@link List<Section>} that contains all sections in the system.
     */
    List<Section> findAllSections();

    /**
     * Updates the {@link User}.
     *
     * @param userToUpdate is the {@link User} to update.
     */
    void updateUser(User userToUpdate);

    /**
     * Updates the {@link Report}.
     *
     * @param reportToUpdate is the {@link Report} to update.
     */
    void updateReport(Report reportToUpdate);

    /**
     * Creates a new {@link Conference} in the system.
     *
     * @param conferenceToCreate is the {@link Conference} to create.
     * @throws DuplicateException if there is the {@link Conference}
     *                            with the conferenceTitle of the {@param conferenceToCreate}
     */
    void createConference(Conference conferenceToCreate) throws DuplicateException;

    /**
     * Creates a new {@link Section} in the system.
     *
     * @param sectionToCreate is the {@link Section} to create.
     * @throws DuplicateException if there is the {@link Section}
     *                            with the sectionName of the {@param sectionToCreate}
     *                            and there is a violation of unique constrains in the database.
     */
    void createSection(Section sectionToCreate) throws DuplicateException;

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
     * Updates the {@link Conference}.
     *
     * @param conferenceToUpdate is the {@link Conference} to update.
     */
    void updateConference(Conference conferenceToUpdate);

    /**
     * Updates the {@link Section}.
     *
     * @param sectionToUpdate is the {@link Section} to update.
     */
    void updateSection(Section sectionToUpdate);

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
     *
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
     * Sets a new {@link Role} to the {@link User} with id equals {@param userId}
     *
     * @param userId  is the id of the {@link User} which {@link Role} is to update.
     * @param newRole is the {@link Role} to set.
     */
    void updateUserRole(Long userId, Long newRole);

    /**
     * Returns all {@link Report}s in the system.
     *
     * @return {@link List<Report>} that contains all reports in the system.
     */
    List<Report> findAllReports();

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

    /**
     * Deletes the {@link Report}.
     *
     * @param reportId is the id value of the {@link Report} to delete.
     */
    void deleteReport(Long reportId);

    /**
     * Finds all {@link Report}s in the system that have with the parameter applicant equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Report}s parameter applicant to find.
     * @return {@link List<Report>} that contains all the {@link Report}s in the system with the parameter applicant
     * equals {@param userId}.
     */
    List<Report> findAllReportsByUserId(Long userId);

    /**
     * Deletes the {@link User}.
     *
     * @param userId is the id value of the {@link User} to delete.
     */
    void deleteUser(Long userId);

    /**
     * Finds all {@link Conference}s in the system with the parameter managerConf equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Conference}s parameter managerConf to find.
     * @return {@link List<Conference>} that contains all the {@link Conference}s in the system with the parameter
     * managerConf equals {@param userId}.
     */
    List<Conference> findAllConferencesWhereUserIsManager(Long userId);

    /**
     * Finds all {@link Section}s in the system with the parameter managerSect equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Section}s parameter managerSect to find.
     * @return {@link List<Conference>} that contains all the {@link Section}s in the system with the parameter
     * managerSect equals {@param userId}.
     */
    List<Section> findAllSectionsWhereUserIsManager(Long userId);
}