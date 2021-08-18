package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;

import java.util.List;
import java.util.Optional;

/**
 * Implements 'delete_user' action. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class DeleteUser implements Command {

    private final UserService service;
    private final Validator validator;

    private static class DeleteUserHolder {
        private final static DeleteUser instance
                = new DeleteUser();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static DeleteUser getInstance() {
        return DeleteUser.DeleteUserHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private DeleteUser() {
        this.service = UserService.retrieve();
        this.validator = Validator.retrieve();
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long userId = Long.valueOf(request.getParameter(ApplicationConstants.USER_ID_PARAMETER_NAME));
        final String userRole = String.valueOf(request.getParameter(ApplicationConstants.USER_ROLE_PARAMETER_NAME));
        final Long deleterId = Long.valueOf(request.getParameter(ApplicationConstants.DELETER_ID_PARAMETER_NAME));
        final String deleterRole = String.valueOf(request.getParameter(ApplicationConstants.DELETER_ROLE_PARAMETER_NAME));
        Optional<User> userFromDB = service.findUserByID(userId);

        // parameters validation
        if (!validator.isUserWithIdExistInSystem(userId)
                || !validator.isRoleWithSuchNameExistInSystem(userRole)
                || !validator.isUserWithIdExistInSystem(deleterId)
                || !validator.isRoleWithSuchNameExistInSystem(deleterRole)) {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        // validation of the matching user with themself from DB
        if (userFromDB.isPresent()) {
            if (!userFromDB.get().getRole().getName().equals(userRole)) {
                return prepareErrorPageBackToMainPage(request,
                        ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
            }
        } else {
            return prepareErrorPageBackToMainPage(request,
                    ApplicationConstants.INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG);
        }

        final List<User> users = service.findAllUsers();

        // validation of permission to delete
        if (!deleterId.equals(userId) && !deleterRole.equals(ApplicationConstants.ADMIN_CONSTANT)) {
            return prepareErrorPage(request, ApplicationConstants.NO_PERMISSION_TO_UPDATE_USER_MSG);
        }

        // check whether the Role 'ADMIN' is deleted
        // Optional<User> userFromDatabase = service.findUserByID(id);
        Role userRoleFromBD = userFromDB.get().getRole();
        if (userRoleFromBD.getName().equals(ApplicationConstants.ADMIN_CONSTANT)) {
            return prepareErrorPage(request, ApplicationConstants.NO_PERMISSION_TO_DELETE_ADMIN_MSG);
        }

        List<Report> allDeletedUserReports = service.findAllReportsByUserId(userId);
        if (!userRoleFromBD.getName().equals(ApplicationConstants.MANAGER_CONSTANT)
                && !userRoleFromBD.getName().equals(ApplicationConstants.ADMIN_CONSTANT)) {
            service.deleteUser(userId);
            for (Report report : allDeletedUserReports
            ) {
                service.deleteReport(report.getId());
            }
            if (!deleterId.equals(userId)) {
                final List<User> usersAfterDelete = service.findAllUsers();
                request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, usersAfterDelete);
                return ApplicationConstants.SHOW_USERS_PAGE_RESPONSE;
            } else {
                request.invalidateCurrentSession();
                return ApplicationConstants.MAIN_PAGE_REDIRECT;
            }
        } else if (userRoleFromBD.getName().equals(ApplicationConstants.ADMIN_CONSTANT)) {
            return prepareErrorPage(request, ApplicationConstants.NO_PERMISSION_TO_DELETE_ADMIN_MSG);
        }
        service.deleteUser(userId);
        List<Conference> allConferencesWhereUserIsManager = service.findAllConferencesWhereUserIsManager(userId);
        List<Section> allSectionsWhereUserIsManager = service.findAllSectionsWhereUserIsManager(userId);

        List<Conference> conferences = service.findAllConferences();
        Long adminId = null;
        for (User user : users
        ) {
            if (user.getRole().equals(Role.ADMIN)) {
                adminId = user.getId();
                break;
            }
        }
        for (Section section: allSectionsWhereUserIsManager
        ) {
            Long conferenceManager = null;
            for (Conference conference: conferences
            ) {
                if (section.getConferenceId().equals(conference.getId())) {
                    if (conference.getManagerConf().equals(userId)) {
                        conferenceManager = adminId;
                    } else {
                        conferenceManager = conference.getManagerConf();
                    }
                }
            }
            Section updatedSection = new Section(section.getId(),
                    section.getConferenceId(),
                    section.getSectionName(),
                    conferenceManager);
            service.updateSection(updatedSection);
        }

        for (Conference conference : allConferencesWhereUserIsManager
        ) {
            Conference updatedConference = new Conference(conference.getId(), conference.getConferenceTitle(), adminId);
            service.updateConference(updatedConference);
        }
        for (Report report : allDeletedUserReports
        ) {
            service.deleteReport(report.getId());
        }
        if (!deleterId.equals(userId)) {
            final List<User> usersAfterDelete = service.findAllUsers();
            request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, usersAfterDelete);
            return ApplicationConstants.SHOW_USERS_PAGE_RESPONSE;
        } else {
            request.invalidateCurrentSession();
            return ApplicationConstants.MAIN_PAGE_REDIRECT;
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request,
                                             String errorMessage) {
        final Long id = Long.valueOf(request.getParameter(ApplicationConstants.ID_PARAMETER_NAME));
        final Optional<User> user = service.findUserByID(id);
        request.setAttribute(ApplicationConstants.USER_ATTRIBUTE_NAME, user);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);

        return ApplicationConstants.UPDATE_USER_ERROR_RESPONSE;
    }

    private CommandResponse prepareErrorPageBackToMainPage(CommandRequest request,
                                                           String errorMessage) {
        final List<Conference> conferences = service.findAllConferences();
        request.setAttribute(ApplicationConstants.CONFERENCES_ATTRIBUTE_NAME, conferences);
        final List<User> users = service.findAllUsers();
        request.setAttribute(ApplicationConstants.USERS_ATTRIBUTE_NAME, users);
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, errorMessage);
        return ApplicationConstants.UPDATE_USER_ERROR_RESPONSE_TO_MAIN_PAGE;
    }
}