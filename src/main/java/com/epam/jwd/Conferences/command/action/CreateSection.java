package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CreateSection implements Command {
    private static final Logger logger = LogManager.getLogger(CreateSection.class);

    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    private static final String MANAGER_SECTION_PARAMETER_NAME = "managerSect";
    private static final CommandResponse CREATE_NEW_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final CommandResponse SECTION_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    private static final String DUPLICATE_SECTION_MESSAGE
            = "The section with such an section name title already exist in the system. Please choose an other section name.";
    private static final String CONFERENCE_MANAGER_ATTRIBUTE_NAME = "conferenceManager";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
    private static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private CreateSection() {
        service = UserService.retrieve();
    }

    private static class CreateSectionHolder {
        private final static CreateSection instance
                = new CreateSection();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static CreateSection getInstance() {
        return CreateSection.CreateSectionHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        final String managerSect = request.getParameter(MANAGER_SECTION_PARAMETER_NAME);
        final Long creatorId = Long.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));

        final List<User> users = service.findAllUsers();
        final List<Conference> conferences = service.findAllConferences();

        Long conferenceManagerId = null;
        for (Conference conference : conferences
        ) {
            if (conference.getId() == conferenceId) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        if (!creatorRole.equals("ADMIN") && !(creatorId == conferenceManagerId)) {
            return prepareErrorPage(request,
                    "You have no permission to create a new section. Please DO NOT try again");
        } else if (sectionName == null || sectionName.trim().equals("")) {
            return prepareErrorPage(request,
                    "Section name should be not empty or contains only spaces. Please try again");
        } else if (!isStringValid(sectionName)) {
            return prepareErrorPage(request,
                    "The entered section name is not valid. It should contain only latin letters. Please try again");
        } else if (sectionName.length() > MAX_LENGTH_OF_SECTION_NAME_IN_DB) {
            return prepareErrorPage(request,
                    "The entered section name is too long. It should be not more as "
                            + MAX_LENGTH_OF_SECTION_NAME_IN_DB
                            + " signs. Please try again");
        }

        Long managerId = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(managerSect)) {
                managerId = user.getId();
            }
        }
        Section sectionToCreate = new Section(1L, conferenceId, sectionName, managerId);
        try {
            service.createSection(sectionToCreate);
            final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
            request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
            final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
            request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
            request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            final List<User> users1 = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, users1);

            request.setAttribute(CONFERENCE_MANAGER_ATTRIBUTE_NAME, conferenceManagerId);
            return SECTION_CREATION_SUCCESS_RESPONSE;
        } catch (DuplicateException e) {
            logger.info(DUPLICATE_SECTION_MESSAGE);
            return prepareErrorPage(request, DUPLICATE_SECTION_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        final String conferenceTitle = request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME);
        final String conferenceId = request.getParameter(CONFERENCE_ID_PARAMETER_NAME);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        final List<Conference> conferences = service.findAllConferences();
        Long conferenceManagerId = null;
        for (Conference conference: conferences
        ) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);

        return CREATE_NEW_SECTION_ERROR_RESPONSE;
    }

    private boolean isStringValid(String toValidate) {
        byte[] byteArrray = toValidate.getBytes();
        return isUTF8(byteArrray);
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}
