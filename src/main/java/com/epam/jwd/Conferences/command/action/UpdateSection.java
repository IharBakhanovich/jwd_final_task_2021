package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class UpdateSection implements Command {

    private static final String MANAGER_SECTION_NICKNAME_PARAMETER_NAME = "managerSectNickname";
    public static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    public static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
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

    private static final CommandResponse SECTION_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");
    public static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;

    private static final CommandResponse SECTION_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    UpdateSection() {
        service = UserService.retrieve();
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
            return prepareErrorPage(request,
                    "You have no permission to update this section. Please DO NOT try again");
        } else if (sectionName == null || sectionName.trim().equals("")) {
            return prepareErrorPage(request,
                    "Section name should not be empty or contains only spaces. Please try again");
        } else if (!isStringValid(sectionName)) {
            return prepareErrorPage(request,
                    "The entered section name is not valid. It should contain only latin letters. Please try again");
        } else if (sectionName.length() > MAX_LENGTH_OF_SECTION_NAME_IN_DB) {
            return prepareErrorPage(request,
                    "The entered section name is too long. It should be not more as "
                            + MAX_LENGTH_OF_SECTION_NAME_IN_DB
                            + " signs. Please try again");
        }
        Section sectionToUpdate = new Section(sectionId, conferenceId, sectionName, sectionManagerId);
        service.updateSection(sectionToUpdate);
        request.setAttribute(CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_TO_SECTIONS, conferenceManagerId);
        final List<Section> sections = service.findAllSectionsByConferenceID(conferenceId);
        request.setAttribute(SECTIONS_ATTRIBUTE_NAME, sections);
        request.setAttribute(CONFERENCE_TITLE_ATTRIBUTE_NAME, conferenceTitle);
        request.setAttribute(CONFERENCE_ID_ATTRIBUTE_NAME, conferenceId);
        final List<User> users = service.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);

        return SECTION_UPDATE_SUCCESS_RESPONSE;
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

    private boolean isStringValid(String toValidate) {
        byte[] byteArray = toValidate.getBytes();
        return isUTF8(byteArray);
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}