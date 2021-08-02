package com.epam.jwd.Conferences.command.page;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.service.UserService;
import java.util.Optional;

public class ShowUserPage implements Command {

    private static final CommandResponse SHOW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    private static final String ID_PARAMETER_NAME = "id";
    private static final String USER_ATTRIBUTE_NAME = "user";

    private final UserService service;

    private static class ShowUserPageHolder {
        private final static ShowUserPage instance
                = new ShowUserPage();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static ShowUserPage getInstance() {
        return ShowUserPage.ShowUserPageHolder.instance;
    }

    // the private default constructor, to not create the instance of the class with 'new' outside the class
    private ShowUserPage() {
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
        final Optional<User> user = service.findUserByID(id);

        request.setAttribute(USER_ATTRIBUTE_NAME, user);
        return SHOW_USER_PAGE_RESPONSE;
    }
}
