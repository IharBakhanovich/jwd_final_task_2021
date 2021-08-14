package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class CreateSection implements Command {
    private static final Logger logger = ApplicationConstants.LOGGER_FOR_CREATE_SECTION; //LogManager.getLogger(CreateSection.class);

//    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
//    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
//    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
//    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
//    private static final String MANAGER_SECTION_PARAMETER_NAME = "managerSect";
//    private static final CommandResponse CREATE_NEW_SECTION_ERROR_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
//    private static final CommandResponse CREATE_SECTION_ERROR_RESPONSE_TO_MAIN_PAGE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
//    private static final String ERROR_ATTRIBUTE_NAME = "error";
//    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
//    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
//    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
//    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
//    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
//    private static final String USERS_ATTRIBUTE_NAME = "users";
//    private static final CommandResponse SECTION_CREATION_SUCCESS_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
//    private static final String DUPLICATE_SECTION_MESSAGE
//            = "The section with such an section name title already exist in the system. Please choose an other section name.";
//    private static final String CONFERENCE_MANAGER_ATTRIBUTE_NAME = "conferenceManager";
//    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
//    private static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;
//    private static final String INVALID_SECTION_NAME_MSG = "SectionNameShouldNotBeEmptyMSG";
//    private static final String INVALID_SECTION_NAME_NOT_UTF8_MSG = "SectionNameShouldContainOnlyLatinSignsMSG";
//    private static final String NO_PERMISSION_TO_CREATE_SECTION_MSG = "YouHaveNoPermissionToCreateASectionMSG";
//    private static final String INVALID_SECTION_NAME_TOO_LONG_MSG = "SectionNameIsTooLongMSG";
//    private static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";

    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private CreateSection() {
        service = UserService.retrieve();
        validator = Validator.retrieve();
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
        final Long conferenceId = Long.valueOf(request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME));
        final String sectionName = request.getParameter(ApplicationConstants.SECTION_NAME_PARAMETER_NAME);
        final String managerSect = request.getParameter(ApplicationConstants.MANAGER_SECTION_PARAMETER_NAME);
        final Long creatorId = Long.valueOf(request.getParameter(ApplicationConstants.CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(ApplicationConstants.CREATOR_ROLE_PARAMETER_NAME));
        final String conferenceTitleToCheck = String.valueOf(request.getParameter(ApplicationConstants.CONFERENCE_TITLE_PARAMETER_NAME));

        final List<User> users = service.findAllUsers();
        final List<Conference> conferences = service.findAllConferences();

        Long conferenceManagerId = null;
        for (Conference conference : conferences
        ) {
            if (conference.getId().equals(conferenceId)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        // validation of the parameters (whether they exist in the system)
        if (!validator.isConferenceExistInSystem(conferenceId)
                || !validator.isUserWithIdExistInSystem(creatorId)
                || !validator.isUserWithNicknameExistInSystem(managerSect)
                || !validator.isUserWithIdExistInSystem(conferenceManagerId)
                || !validator.isRoleWithSuchNameExistInSystem(creatorRole)
                || !validator.isConferenceTitleAndIdFromTheSameConference(conferenceId, conferenceTitleToCheck)) {
            return prepareErrorPageBackToMainPage(request, ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        if (!creatorRole.equals("ADMIN") && !(creatorId.equals(conferenceManagerId))) {
            return prepareErrorPage(request, ApplicationConstants.NO_PERMISSION_TO_CREATE_SECTION_MSG);
        } else if (sectionName == null || sectionName.trim().equals("")) {
            return prepareErrorPage(request, ApplicationConstants.INVALID_SECTION_NAME_MSG);
        } else if (!validator.isStringValid(sectionName)) {
            return prepareErrorPage(request, ApplicationConstants.INVALID_SECTION_NAME_NOT_UTF8_MSG);
        } else if (!validator.isLengthValid(sectionName, ApplicationConstants.MAX_LENGTH_OF_SECTION_NAME_IN_DB)) {
            return prepareErrorPage(request, ApplicationConstants.INVALID_SECTION_NAME_TOO_LONG_MSG);
        }

        Long managerId = null;
        Role sectionManagerRole = null;
        for (User user : users
        ) {
            if (user.getNickname().equals(managerSect)) {
                managerId = user.getId();
                sectionManagerRole = user.getRole();
            }
        }
        Section sectionToCreate = new Section(1L, conferenceId, sectionName, managerId);
        try {
            service.createSection(sectionToCreate);
            final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
            request.setAttribute(ApplicationConstants.SECTIONS_ATTRIBUTE_NAME, sections);
            final String conferenceTitle = request.getParameter(ApplicationConstants.CONFERENCE_TITLE_PARAMETER_NAME);
            request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
            request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
            final List<User> users1 = service.findAllUsers();
            request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users1);
            request.setAttribute(ApplicationConstants.CONFERENCE_MANAGER_ATTRIBUTE_NAME, conferenceManagerId);
            // sets a role 'MANAGER' to the manager of a new section if is not the Role.Manager
            if (sectionManagerRole != Role.ADMIN && sectionManagerRole != Role.MANAGER) {
                service.updateUserRole(managerId, Role.MANAGER.getId());
            }
            return ApplicationConstants.SECTION_CREATION_SUCCESS_RESPONSE;
        } catch (DuplicateException e) {
            logger.info(ApplicationConstants.DUPLICATE_SECTION_MESSAGE);
            return prepareErrorPage(request, ApplicationConstants.DUPLICATE_SECTION_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        final String conferenceTitle = request.getParameter(ApplicationConstants.CONFERENCE_TITLE_PARAMETER_NAME);
        final String conferenceId = request.getParameter(ApplicationConstants.CONFERENCE_ID_PARAMETER_NAME);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(ApplicationConstants.CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        final List<Conference> conferences = service.findAllConferences();
        Long conferenceManagerId = null;
        for (Conference conference : conferences
        ) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                conferenceManagerId = conference.getManagerConf();
            }
        }
        request.setAttribute(ApplicationConstants.CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);

        return ApplicationConstants.CREATE_NEW_SECTION_ERROR_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.CREATE_SECTION_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}
