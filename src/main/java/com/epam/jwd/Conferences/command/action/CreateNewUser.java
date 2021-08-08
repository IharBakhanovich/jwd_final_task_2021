package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Implements 'create_new_user' action. The singleton.
 */
public class CreateNewUser implements Command {

    private static final Logger logger = LogManager.getLogger(CreateNewUser.class);

    private static final String NICKNAME_PARAMETER_NAME = "nickname";
    private static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String PASSWORD_REPEAT_PARAMETER_NAME = "passwordRepeat";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final CommandResponse CREATE_NEW_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");
    private static final CommandResponse USER_CREATION_SUCCESS_RESPONSE_ADMIN
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    private static final CommandResponse USER_CREATION_SUCCESS_RESPONSE_UNAUTHORISED
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");
    private static final String DUPLICATE_USER_MESSAGE = "The user with such a nickname already exists in the system.";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final int MAX_LENGTH_OF_NICKNAME_IN_DB = 30;
    private static final int MAX_LENGTH_OF_PASSWORD = 32;
    private static final String INVALID_NICKNAME_IS_EMPTY_MSG = "NicknameShouldNotBeEmptyMSG";
    private static final String INVALID_NICKNAME_NOT_UTF8_MSG = "NicknameShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_PASSWORD_IS_EMPTY_MSG = "PasswordShouldNotBeEmptyMSG";
    private static final String INVALID_PASSWORD_NOT_UTF8_MSG = "PasswordShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_FIRST_NAME_IS_TOO_LONG_MSG = "FirstNameIsTooLongMSG";
    private static final String INVALID_NICKNAME_IS_ALREADY_EXIST_IN_SYSTEM_AND_PASSWORD_REPEATED_WRONG_MSG = "NicknameIsAlreadyExistInSystemAndPasswordRepeatedWrongMSG";
    private static final String INVALID_NICKNAME_SUCH_USER_EXISTS_IN_SYSTEM_MSG = "NicknameIsAlreadyExistInSystemMSG";
    private static final String INVALID_PASSWORD_REPEATED_WRONG_MSG = "PasswordRepeatedWrongMSG";
    private static final String INVALID_PASSWORD_TOO_LONG_MSG = "PasswordTooLongMSG";
    private static final String NO_PERMISSION_TO_CREATE_USER_IN_SYSTEM_MSG = "YouHaveNoPermissionToCreateUserInSystemMSG";

    private final UserService service;
    private final Validator validator;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private CreateNewUser() {
        service = UserService.retrieve();
        validator = Validator.retrieve();
    }

    private static class CreateNewUserHolder {
        private final static CreateNewUser instance
                = new CreateNewUser();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static CreateNewUser getInstance() {
        return CreateNewUser.CreateNewUserHolder.instance;
    }

    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final String nickname = request.getParameter(NICKNAME_PARAMETER_NAME);
        final String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        final String passwordRepeat = request.getParameter(PASSWORD_REPEAT_PARAMETER_NAME);
        final String creatorId = String.valueOf(request.getParameter(CREATOR_ID_PARAMETER_NAME));
        final String creatorRole = String.valueOf(request.getParameter(CREATOR_ROLE_PARAMETER_NAME));
        final List<User> users = service.findAllUsers();

        if (nickname == null || nickname.trim().equals("")) {
            return prepareErrorPage(request, INVALID_NICKNAME_IS_EMPTY_MSG);
        } else if (password == null || password.trim().equals("")) {
            return prepareErrorPage(request, INVALID_PASSWORD_IS_EMPTY_MSG);
        } else if (!validator.isLengthValid(password, MAX_LENGTH_OF_PASSWORD)) {
            return prepareErrorPage(request, INVALID_PASSWORD_TOO_LONG_MSG);
        } else if (!validator.isStringValid(nickname)) {
            return prepareErrorPage(request, INVALID_NICKNAME_NOT_UTF8_MSG);
        } else if (!validator.isStringValid(password)) {
            return prepareErrorPage(request, INVALID_PASSWORD_NOT_UTF8_MSG);
        }

        if (creatorRole == null) {
            return prepareErrorPage(request, NO_PERMISSION_TO_CREATE_USER_IN_SYSTEM_MSG);
        }

        if (!validator.isLengthValid(nickname, MAX_LENGTH_OF_NICKNAME_IN_DB)) {
            return prepareErrorPage(request, INVALID_FIRST_NAME_IS_TOO_LONG_MSG);
        }

        for (User user : users
        ) {
            if (user.getNickname().equals(nickname) && !password.equals(passwordRepeat)) {
                return prepareErrorPage(request, INVALID_NICKNAME_IS_ALREADY_EXIST_IN_SYSTEM_AND_PASSWORD_REPEATED_WRONG_MSG);
            } else if (user.getNickname().equals(nickname)) {
                return prepareErrorPage(request, INVALID_NICKNAME_SUCH_USER_EXISTS_IN_SYSTEM_MSG);
            } else if (!password.equals(passwordRepeat)) {
                return prepareErrorPage(request, INVALID_PASSWORD_REPEATED_WRONG_MSG);
            }
        }
        User userToCreate = new User(nickname, password);
        try {
            service.createUser(userToCreate);
            final List<User> updatedUsersList = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, updatedUsersList);
            if (creatorRole.equals("ADMIN")) {
                return USER_CREATION_SUCCESS_RESPONSE_ADMIN;
            } else {
                return USER_CREATION_SUCCESS_RESPONSE_UNAUTHORISED;
            }

        } catch (DuplicateException e) {
            logger.info(DUPLICATE_USER_MESSAGE);
            return prepareErrorPage(request, DUPLICATE_USER_MESSAGE);
        }
    }

    private CommandResponse prepareErrorPage(CommandRequest request, String errorMessage) {
        request.setAttribute(ERROR_ATTRIBUTE_NAME, errorMessage);
        return CREATE_NEW_USER_ERROR_RESPONSE;
    }
}