package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;
import java.util.Optional;

public class UpdateSection implements Command {

    private static final String MANAGER_SECTION_NICKNAME_PARAMETER_NAME = "managerSectNickname";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
    private static final String CONFERENCE_MANAGER_ID_PARAMETER_NAME = "conferenceManagerId";

    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_TO_SECTIONS = "conferenceManager";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String SECTION_MANAGER_ID_ATTRIBUTE_NAME = "sectionManagerId";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String INVALID_SECTION_NAME_MSG = "SectionNameShouldNotBeEmptyMSG";
    private static final String INVALID_SECTION_NAME_NOT_UTF8_MSG = "SectionNameShouldContainOnlyLatinSignsMSG";
    private static final String NO_PERMISSION_TO_UPDATE_SECTION_MSG = "YouHaveNoPermissionToUpdateASectionMSG";
    private static final String INVALID_SECTION_NAME_TOO_LONG_MSG = "SectionNameIsTooLongMSG";

    private static final CommandResponse SECTION_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");
    private static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;

    private static final CommandResponse SECTION_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");

    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private UpdateSection() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    private static class UpdateSectionHolder {
        private final static UpdateSection instance
                = new UpdateSection();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static UpdateSection getInstance() {
        return UpdateSection.UpdateSectionHolder.instance;
    }


    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final String sectionManagerNickname = request.getParameter(MANAGER_SECTION_NICKNAME_PARAMETER_NAME);
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final String conferenceTitle = String.valueOf(request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter("creatorRole"));
        final Long creatorId = Long.valueOf(request.getParameter("creatorId"));
        final Long conferenceManagerId = Long.valueOf(request.getParameter(CONFERENCE_MANAGER_ID_PARAMETER_NAME));
        Long sectionManagerId = fetchSectionManagerId(sectionManagerNickname);
        if (!creatorRole.equals("ADMIN") && !creatorId.equals(conferenceManagerId) && !creatorId.equals(sectionManagerId)) {
            return prepareErrorPage(request, NO_PERMISSION_TO_UPDATE_SECTION_MSG);
        } else if (sectionName == null || sectionName.trim().equals("")) {
            return prepareErrorPage(request, INVALID_SECTION_NAME_MSG);
        } else if (!validator.isStringValid(sectionName)) {
            return prepareErrorPage(request, INVALID_SECTION_NAME_NOT_UTF8_MSG);
        } else if (!validator.isLengthValid(sectionName, MAX_LENGTH_OF_SECTION_NAME_IN_DB)) {
            return prepareErrorPage(request, INVALID_SECTION_NAME_TOO_LONG_MSG);
        }
        Section sectionToUpdate = new Section(sectionId, conferenceId, sectionName, sectionManagerId);
        // to have sections and the status of the former section manager before the section update
        List<Section> sectionsBeforeUpdate = service.findAllSections();
        boolean isTheManagerOfThisSectionRemainsManager = isTheManagerOfThisSectionRemainsManager(sectionId);
        Long formerManagerOrThisSectionId = null;
        for (Section section : sectionsBeforeUpdate
        ) {
            if (section.getId().equals(sectionId)) {
                formerManagerOrThisSectionId = section.getManagerSect();
            }
        }
        Optional<User> oldSectionManager = service.findUserByID(formerManagerOrThisSectionId);
        Role currentRoleOfFormerSectionManager = oldSectionManager.get().getRole();
        // to update the section
        service.updateSection(sectionToUpdate);
        // to update role of the newSectionManager
        Optional<User> newSectionManager = service.findUserByID(sectionManagerId);
        Role currentRoleOfNewSectionManager = newSectionManager.get().getRole();
        if (currentRoleOfNewSectionManager != Role.ADMIN && currentRoleOfNewSectionManager != Role.MANAGER) {
            service.updateUserRole(sectionManagerId, Role.MANAGER.getId());
        }
        // to check if it needed and update the role of the formerSectionManager
        if (!isTheManagerOfThisSectionRemainsManager && currentRoleOfFormerSectionManager != Role.USER) {
            service.updateUserRole(formerManagerOrThisSectionId, Role.USER.getId());
        }
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_TO_SECTIONS, conferenceManagerId);
        final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);

        return SECTION_UPDATE_SUCCESS_RESPONSE;
    }

    private boolean isTheManagerOfThisSectionRemainsManager(Long sectionId) {
        // to prove whether the former Manager of this section is still the Manager of another sections or conferences
        boolean isThisManagerTheManagerOfAnotherConferenceOrSection = false;
        List<Section> sectionsBeforeUpdate = service.findAllSections();
        List<Conference> conferences = service.findAllConferences();

        // to find former section manager
        User formerSectionManager = null;
        Long formerManagerOrThisSectionId = null;
        for (Section section : sectionsBeforeUpdate
        ) {
            if (section.getId().equals(sectionId)) {
                formerManagerOrThisSectionId = section.getManagerSect();
            }
        }
        // to prove whether this Manager is the manager of the another section
        for (Section section : sectionsBeforeUpdate
        ) {
            if (section.getManagerSect().equals(formerManagerOrThisSectionId) && !section.getId().equals(sectionId)) {
                isThisManagerTheManagerOfAnotherConferenceOrSection = true;
                break;
            }
        }

        // to prove whether this Manager is the manager of the another conference
        for (Conference conference : conferences
        ) {
            if (conference.getManagerConf().equals(formerManagerOrThisSectionId)) {
                isThisManagerTheManagerOfAnotherConferenceOrSection = true;
                break;
            }
        }
        return isThisManagerTheManagerOfAnotherConferenceOrSection;
    }

    private Long fetchSectionManagerId(String sectionManagerNickname) {
        Long sectionManagerid = null;
        final List<User> users = service.findAllUsers();
        for (User user : users
        ) {
            if (user.getNickname().equals(sectionManagerNickname)) {
                sectionManagerid = user.getId();
            }
        }
        return sectionManagerid;
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        final List<User> users = service.findAllUsers();
        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
        final String conferenceTitle = String.valueOf(request.getParameter(CONFERENCE_TITLE_PARAMETER_NAME));
        final Long sectionId = Long.valueOf(request.getParameter(SECTION_ID_PARAMETER_NAME));
        final String sectionName = request.getParameter(SECTION_NAME_PARAMETER_NAME);
        final String sectionManagerNickname = request.getParameter(MANAGER_SECTION_NICKNAME_PARAMETER_NAME);
        Long sectionManagerId = fetchSectionManagerId(sectionManagerNickname);
        final Long conferenceManagerId = Long.valueOf(request.getParameter(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME));
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(SECTION_ID_ATTRIBUTE_NAME, sectionId);
        request.setAttribute(SECTION_NAME_ATTRIBUTE_NAME, sectionName);
        request.setAttribute(SECTION_MANAGER_ID_ATTRIBUTE_NAME, sectionManagerId);
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME, conferenceManagerId);
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
//        final Long conferenceId = Long.valueOf(request.getParameter(CONFERENCE_ID_PARAMETER_NAME));
//        final List<User> users = service.findAllUsers();
//        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
//        final List<Conference> conferences = service.findAllConferences();
//        for (Conference conference: conferences
//        ) {
//            if (conference.getId().equals(conferenceId)) {
//                request.setAttribute(MANAGER_CONFERENCE_ATTRIBUTE_NAME, conference.getManagerConf());
//            }
//        }
//        request.setAttribute(CONFERENCES_ATTRIBUTE_NAME, conferences);
//        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        return SECTION_UPDATE_ERROR_RESPONSE;
    }
}