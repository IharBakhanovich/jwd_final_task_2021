package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;

import java.util.List;

public interface UserService {

    void create(User user);

    boolean canLogIn(User user);

    User findByLogin(String login);

    List<User> findAll();

    void clean();

    static UserService retrieve() {
        return AppUserService.getInstance();
    }

    List<Conference> findAllConferences();

    List<Section> findAllSectionsByConferenceID(Long id);

    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);
}
