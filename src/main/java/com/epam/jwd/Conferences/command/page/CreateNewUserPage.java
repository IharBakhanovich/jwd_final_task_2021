package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class CreateNewUserPage implements Command {

    private static final Logger logger = LogManager.getLogger(CreateNewUserPage.class);

    private static final String NICKNAME_PARAMETER_NAME = "nickname";
    private static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String PASSWORD_REPEAT_PARAMETER_NAME = "passwordRepeat";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final CommandResponse CREATE_NEW_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");
    private static final CommandResponse USER_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    private static final String DUPLICATE_USER_MESSAGE = "The user with such a nickname already exist in the system. Please choose an other nick.";
    private static final String USERS_ATTRIBUTE_NAME = "users";

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    CreateNewUserPage() {
        service = UserService.retrieve();
    }

    private static class CreateNewUserPageHolder {
        private final static CreateNewUserPage instance
                = new CreateNewUserPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static CreateNewUserPage getInstance() {
        return CreateNewUserPage.CreateNewUserPageHolder.instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String nickname = request.getParameter(NICKNAME_PARAMETER_NAME);
        final String password = request.getParameter(PASSWORD_PARAMETER_NAME);
        final String passwordRepeat = request.getParameter(PASSWORD_REPEAT_PARAMETER_NAME);
        final List<User> users = service.findAllUsers();
        if (nickname == null || nickname.trim().equals("")) {
            return prepareErrorPage(request, "Nickname should be not empty or contains only spaces. Please try again");
        } else if (password == null || password.trim().equals("")) {
            return prepareErrorPage(request, "Password should be not empty or contains only spaces. Please try again");
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
            service.create(userToCreate);
            final List<User> updatedUsersList = service.findAllUsers();
            request.setAttribute(USERS_ATTRIBUTE_NAME, updatedUsersList);
            return USER_CREATION_SUCCESS_RESPONSE;
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
