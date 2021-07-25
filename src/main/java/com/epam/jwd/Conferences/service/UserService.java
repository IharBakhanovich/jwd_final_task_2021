package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void create(User user);

    boolean canLogIn(User user);

    User findByLogin(String login);

    List<User> findAllUsers();

    void clean();

    static UserService retrieve() {
        return AppUserService.getInstance();
    }

    List<Conference> findAllConferences();

    List<Section> findAllSectionsByConferenceID(Long id);

    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);

    Optional<User> findUserByID(Long id);

    Optional<Report> findReportByID(Long id);

    List<Section> findAllSections();
}
