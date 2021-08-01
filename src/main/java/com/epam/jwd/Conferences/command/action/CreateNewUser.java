package com.epam.jwd.Conferences.command.action;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String DUPLICATE_USER_MESSAGE = "The user with such a nickname already exist in the system. Please choose an other nick.";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    public static final int MAX_LENGTH_OF_NICKNAME_IN_DB = 30;

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    CreateNewUser() {
        service = UserService.retrieve();
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
            return prepareErrorPage(request, "Nickname should be not empty or contains only spaces. Please try again");
        } else if (password == null || password.trim().equals("")) {
            return prepareErrorPage(request, "Password should be not empty or contains only spaces. Please try again");
        } else if (!isStringValid(nickname)) {
            return prepareErrorPage(request, "The entered nickname is not valid. It should contain only latin letters. Please try again");
        } else if (!isStringValid(password)) {
            return prepareErrorPage(request, "The entered password is not valid. It should contain only latin letters. Please try again");
        }

        if (nickname.length() > MAX_LENGTH_OF_NICKNAME_IN_DB) {
            return prepareErrorPage(request,
                    "The entered first name is too long. It should be not more as "
                            + MAX_LENGTH_OF_NICKNAME_IN_DB
                            + " signs. Please try again"
            );
        }

        for (User user : users
        ) {
            if (user.getNickname().equals(nickname) && !password.equals(passwordRepeat)) {
                return prepareErrorPage(request, "The user with such a nickname already exist in the system and password was repeated wrong.");
            } else if (user.getNickname().equals(nickname)) {
                return prepareErrorPage(request, DUPLICATE_USER_MESSAGE);
            } else if (!password.equals(passwordRepeat)) {
                return prepareErrorPage(request, "The password was repeated wrong. Please try again.");
            }
        }
        User userToCreate = new User(nickname, password);
        try {
            service.createUser(userToCreate);
            final List<User> updatedUsersList = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, updatedUsersList);
            if(creatorRole.equals("ADMIN")){
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

    private boolean isStringValid(String toValidate) {
        byte[] byteArrray = toValidate.getBytes();
        return isUTF8(byteArrray);
    }

    private static boolean isUTF8(final byte[] inputBytes) {
        final String converted = new String(inputBytes, StandardCharsets.UTF_8);
        final byte[] outputBytes = converted.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(inputBytes, outputBytes);
    }

    private boolean validatePasswordComplexity(String toValidate) {
        String password = null;
        password = toValidate.trim();
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
}