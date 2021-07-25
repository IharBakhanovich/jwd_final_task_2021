package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;

import java.util.List;


public class ShowUsersPage implements Command {

    private static final CommandResponse SHOW_USERS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "WEB-INF/jsp/users.jsp");

    public static final String USERS_ATTRIBUTE_NAME = "users";

    // the AppService, that communicates with the repo
    private final UserService userService;

    private static class ShowUsersPageHolder {
        private final static ShowUsersPage instance
                = new ShowUsersPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowUsersPage getInstance() {
        return ShowUsersPage.ShowUsersPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowUsersPage() {
        this.userService = UserService.retrieve();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        //перед тем как отдать респонс нужно в атрибуты HttpServletRequest положить пользователя
        //т.е. в CommandRequest (интерфейс) будет метод void setAttribute()
        //TODO из репозитория с помощью сервиса забрать конференции о положить в аттрибут users
        final List<User> users = userService.findAllUsers();
        request.setAttribute(USERS_ATTRIBUTE_NAME, users);
        return SHOW_USERS_PAGE_RESPONSE;
    }
}
