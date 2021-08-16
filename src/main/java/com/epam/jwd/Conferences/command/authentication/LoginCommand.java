package com.epam.jwd.Conferences.command.authentication;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * Implements 'login' command. The singleton.
 *
 * @author Ihar Bakhanovich
 */
public class LoginCommand implements Command {

//    private static final String LOGIN_PARAM_NAME = "login";
//    private static final String PASSWORD_PARAM_NAME = "password";
//    private static final String ERROR_ATTRIBUTE_NAME = "error";
//    private static final String INVALID_CREDENTIALS_MSG = "InvalidCredentialsMSG";
//    private static final CommandResponse LOGIN_ERROR_RESPONSE
//            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");
//    // редиректаемся на index.jsp, после чего должен произойти get
//    private static final CommandResponse LOGIN_SUCCESS_RESPONSE
//            = CommandResponse.getCommandResponse(true, "index.jsp");
//
//    private static final String USER_NAME_SESSION_ATTRIBUTE = "userName";
//    private static final String USER_ROLE_SESSION_ATTRIBUTE = "userRole";
//    private static final String USER_ID_SESSION_ATTRIBUTE = "userId";

    private final UserService service;

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private LoginCommand() {
        service = UserService.retrieve();
    }

    private static class LoginCommandHolder {
        private final static LoginCommand instance
                = new LoginCommand();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static LoginCommand getInstance() {
        return LoginCommand.LoginCommandHolder.instance;
    }


    /**
     * Executes the command. Returns the commandResponse.
     *
     * @param request a CommandRequest object of this command.
     * @return a CommandResponse object of this command.
     */
    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(ApplicationConstants.LOGIN_PARAM_NAME);
        final String password = request.getParameter(ApplicationConstants.PASSWORD_PARAM_NAME);
        final User user = new User(login, password);
        if (!service.canLogIn(user)) {
            return prepareErrorPage(request);
        }
        return addUserInfoToSession(request, login);
    }

    private CommandResponse prepareErrorPage(CommandRequest request) {
        request.setAttribute(ApplicationConstants.ERROR_ATTRIBUTE_NAME, ApplicationConstants.INVALID_CREDENTIALS_MSG);
        return ApplicationConstants.LOGIN_ERROR_RESPONSE;
    }

    private CommandResponse addUserInfoToSession(CommandRequest request, String login) {
        // fetch a 'sessionLocale' parameter from currentSession
        String valueOfSessionLocaleParameter = null;
        if (request.getCurrentSession().isPresent()) {
            valueOfSessionLocaleParameter = (String) request.getCurrentSession().get().getAttribute(("language"));
        }
        // invalidate current session and create a new one
        request.getCurrentSession().ifPresent(HttpSession::invalidate);
        final HttpSession session = request.createSession();

        // set the the value of 'sessionLocale' parameter into the session
        String finalValueOfSessionLocaleParameter = valueOfSessionLocaleParameter;
        request.getCurrentSession()
                .ifPresent(httpSession -> httpSession.setAttribute("language", finalValueOfSessionLocaleParameter));

        // fetches the current user from service
        final User loggedInUser = service.findByLogin(login);

        // sets only the nickname, the role and the id of the current user to the session
        session.setAttribute(ApplicationConstants.USER_NAME_SESSION_ATTRIBUTE, loggedInUser.getNickname());
        session.setAttribute(ApplicationConstants.USER_ROLE_SESSION_ATTRIBUTE, loggedInUser.getRole());
        session.setAttribute(ApplicationConstants.USER_ID_SESSION_ATTRIBUTE, loggedInUser.getId());

        // redirects on index.jsp with the pattern 'PostRedirectGet', after that will happened 'get'
        return ApplicationConstants.LOGIN_SUCCESS_RESPONSE;
    }
}