package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements 'update_user' action. The singleton.
 */
public class UpdateUser implements Command {

    private static final String ID_PARAMETER_NAME = "id";
    private static final String EMAIL_PARAMETER_NAME = "email";
    private static final String NICKNAME_PARAMETER_NAME = "nickname";
    private static final String FIRSTNAME_PARAMETER_NAME = "firstName";
    private static final String SURNAME_PARAMETER_NAME = "surname";
    private static final String ROLE_PARAMETER_NAME = "role";
    private static final String UPDATER_ID_PARAMETER_NAME = "updaterId";
    private static final String UPDATER_ROLE_PARAMETER_NAME = "updaterRole";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final int MAX_LENGTH_OF_EMAIL_IN_DB = 320;
    private static final int MAX_LENGTH_OF_FIRST_NAME_IN_DB = 30;
    private static final int MAX_LENGTH_OF_SURNAME_IN_DB = 30;
    private static final String NO_PERMISSION_TO_UPDATE_USER_MSG = "YouHaveNoPermissionToUpdateUserMSG";
    private static final String USER_TO_UPDATE_DONT_MATCH_WITH_ITS_ID_MSG = "UserToUpdateDoNotMatchWithItsIdMSG";
    private static final String EMAIL_NOT_VALID_MSG = "EmailIsNotValidMSG";
    private static final String EMAIL_NOT_VALID_UTF8_MSG = "EmailShouldContainOnlyLatinSignsMSG";
    private static final String EMAIL_NOT_VALID_TOO_LONG_MSG = "EmailIsTooLongMSG";
    private static final String INVALID_SURNAME_NOT_UTF8_MSG = "SurnameShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_SURNAME_TOO_LONG_MSG = "SurnameIsTooLongMSG";

    private static final String INVALID_FIRST_NAME_IS_TOO_LONG_MSG = "FirstNameIsTooLongMSG";
    private static final String INVALID_FIRST_NAME_NOT_UTF8_MSG = "FirstNameShouldContainOnlyLatinSignsMSG";

    private static final CommandResponse UPDATE_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    private static final CommandResponse UPDATE_USER_SUCCESS_RESPONSE_FOR_ADMIN
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    private static final CommandResponse UPDATE_USER_SUCCESS_RESPONSE_FOR_USER
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");

    private final UserService service;

    private static class UpdateUserHolder {
        private final static UpdateUser instance
                = new UpdateUser();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static UpdateUser getInstance() {
        return UpdateUser.UpdateUserHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private UpdateUser() {
        this.service = UserService.retrieve();
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final String eMail = String.valueOf(request.getParameter(EMAIL_PARAMETER_NAME));
        final String nickname = String.valueOf(request.getParameter(NICKNAME_PARAMETER_NAME));
        final String firstName = String.valueOf(request.getParameter(FIRSTNAME_PARAMETER_NAME));
        final String surname = String.valueOf(request.getParameter(SURNAME_PARAMETER_NAME));
        final Role role = Role.valueOf(request.getParameter(ROLE_PARAMETER_NAME));
        final String updaterId = String.valueOf(request.getParameter(UPDATER_ID_PARAMETER_NAME));
        final String updaterRole = String.valueOf(request.getParameter(UPDATER_ROLE_PARAMETER_NAME));
        User userFromDB = service.findByLogin(nickname);

        // validation of permission to update
        if (!updaterId.equals(String.valueOf(id)) && !updaterRole.equals("ADMIN")) {
            return prepareErrorPage(request, NO_PERMISSION_TO_UPDATE_USER_MSG);
        }
        // validation of matching the id and login of user to update
        if (!userFromDB.getId().equals(id)) {
            return prepareErrorPage(request, USER_TO_UPDATE_DONT_MATCH_WITH_ITS_ID_MSG);
        }
        // email validation
        else if (!isEmailValid(eMail)) {
            return prepareErrorPage(request, EMAIL_NOT_VALID_MSG);
        }

        // email string validation
        else if (!isStringValid(eMail)) {
            return prepareErrorPage(request, EMAIL_NOT_VALID_UTF8_MSG);
        }

        // string validation (firstName)
        else if (!firstName.trim().equals("")) {
            if (!isStringValid(firstName)) {
                return prepareErrorPage(request, INVALID_FIRST_NAME_NOT_UTF8_MSG);
            }
        }

        // string validation (surname)
        else if (!surname.trim().equals("")) {
            if (!isStringValid(surname)) {
                return prepareErrorPage(request, INVALID_SURNAME_NOT_UTF8_MSG);
            }
        }

        if (eMail.length() > MAX_LENGTH_OF_EMAIL_IN_DB) {
            return prepareErrorPage(request, EMAIL_NOT_VALID_TOO_LONG_MSG);
        }

        if (firstName.length() > MAX_LENGTH_OF_FIRST_NAME_IN_DB) {
            return prepareErrorPage(request, INVALID_FIRST_NAME_IS_TOO_LONG_MSG);
        }

        if (surname.length() > MAX_LENGTH_OF_SURNAME_IN_DB) {
            return prepareErrorPage(request,INVALID_SURNAME_TOO_LONG_MSG);
        }
        User userToUpdate
                = new User(id, eMail, null,
                null, 0, null, false,
                nickname, firstName, surname, role);
        service.updateUser(userToUpdate);
        if (updaterRole.equals("ADMIN")) {
            final List<User> updatedUsersList = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, updatedUsersList);
            return UPDATE_USER_SUCCESS_RESPONSE_FOR_ADMIN;
        } else {
            final Optional<User> user = service.findUserByID(id);

            request.setAttribute(USER_ATTRIBUTE_NAME, user);
            return UPDATE_USER_SUCCESS_RESPONSE_FOR_USER;
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request,
                                             String errorMessage) {
        final Long id = Long.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final Optional<User> user = service.findUserByID(id);
        request.setAttribute(USER_ATTRIBUTE_NAME, user);
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);

        return UPDATE_USER_ERROR_RESPONSE;
    }

    private boolean isStringValid(String toValidate) {
        byte[] byteArray = toValidate.getBytes();
        return isUTF8(byteArray);
    }

    private boolean isEmailValid(String toValidate) {

        String eMail = toValidate.trim();
        Matcher matcher = EMAIL_PATTERN.matcher(eMail);
        return matcher.matches();
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }
}
