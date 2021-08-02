package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;

public class ShowQuestionPage implements Command {

    private static final CommandResponse SHOW_QUESTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/questions.jsp");

    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    public static final String QUESTIONS_ATTRIBUTE_NAME = "questions";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    public static final String MANAGER_ID_ATTRIBUTE_NAME = "managerId";

    private final UserService service;

    private static class ShowQuestionPagePageHolder {
        private final static ShowQuestionPage instance
                = new ShowQuestionPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowQuestionPage getInstance() {
        return ShowQuestionPage.ShowQuestionPagePageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowQuestionPage() {
        this.service = UserService.retrieve();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long managerId = Long.valueOf(request.getParameter(MANAGER_ID_ATTRIBUTE_NAME));
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        final List<Report> questions = service.findAllQuestions(managerId);
        request.setAttribute(QUESTIONS_ATTRIBUTE_NAME, questions);
        final List<Section> sections = service.findAllSections();
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
        return SHOW_QUESTIONS_PAGE_RESPONSE;
    }
}
