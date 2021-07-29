package com.epam.jwd.Conferences.command.page;

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
    public static final String USER_ATTRIBUTE_NAME = "user";

    private static final CommandResponse UPDATE_USER_ERROR_RESPONSE_TO_USER
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    private static final CommandResponse UPDATE_USER_ERROR_RESPONSE_TO_INDEX
            = CommandResponse.getCommandResponse(true, "index.jsp");
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
            return prepareErrorPage(request,
                    "You has no permission to update this user. Please DO NOT try again"
            );
        }
        // validation of matching the id and login of user to update
        if (!userFromDB.getId().equals(id)) {
            final Optional<User> user = service.findUserByID(id);
            request.setAttribute(USER_ATTRIBUTE_NAME, user);
            return prepareErrorPage(request,
                    "User to update don't match with its id. Please try again"
            );
        }
        // email validation
        else if (!isEmailValid(eMail)) {
            final Optional<User> user = service.findUserByID(id);
            request.setAttribute(USER_ATTRIBUTE_NAME, user);
            return prepareErrorPage(request,
                    "Email is not valid. Please try again"
            );
        }
        // string validation (firstName)
        else if (!firstName.trim().equals("")) {
            if (!isStringValid(firstName)) {
                final Optional<User> user = service.findUserByID(id);
                request.setAttribute(USER_ATTRIBUTE_NAME, user);
                return prepareErrorPage(request,
                        "FirstName is not valid. Please try again"
                );
            }
        }
        // string validation (surname)
        else if (!surname.trim().equals("")) {
            if (!isStringValid(surname)) {
                final Optional<User> user = service.findUserByID(id);
                request.setAttribute(USER_ATTRIBUTE_NAME, user);
                return prepareErrorPage(request,
                        "Surname is not valid. Please try again"
                );
            }
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
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);

        return UPDATE_USER_ERROR_RESPONSE_TO_USER;
    }

    private boolean isStringValid(String toValidate) {
        byte[] byteArrray = toValidate.getBytes();
        return isUTF8(byteArrray);
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
