package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    static UserService retrieve() {
        return AppUserService.getInstance();
    }

    void createUser(User user) throws DuplicateException;

    boolean canLogIn(User user);

    User findByLogin(String login);

    List<User> findAllUsers();

    void clean();

    List<Conference> findAllConferences();

    List<Section> findAllSectionsByConferenceID(Long id);

    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);

    Optional<User> findUserByID(Long id);

    Optional<Report> findReportByID(Long id);

    List<Section> findAllSections();

    void updateUser(User userToUpdate);

    void updateReport(Report reportToUpdate);

    void createConference(Conference conferenceToCreate) throws DuplicateException;

    void createSection(Section sectionToCreate) throws DuplicateException;

    void createReport(Report reportToCreate) throws DuplicateException;

    void updateConference(Conference conferenceToUpdate);

    void updateSection(Section sectionToUpdate);

    List<Report> findAllQuestions(Long managerId);

    List<Report> findAllReportsByQuestionId(Long questionReportId);

    List<Report> findApplicantQuestions(Long managerId);

    void updateUserRole(Long userId, Long newRole);

    List<Report> findAllReports();
}
