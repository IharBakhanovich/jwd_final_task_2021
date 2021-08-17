package com.epam.jwd.Conferences.filter;

import com.epam.jwd.Conferences.command.AppCommand;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

//import static com.epam.jwd.Conferences.controller.ApplicationController.COMMAND_PARAM_NAME;
import static com.epam.jwd.Conferences.dto.Role.UNAUTHORIZED;

/**
 * Filters the {@link Role}s of the user and decided whether him is allowed to show the page.
 */
@WebFilter(urlPatterns = "/*")
public class PermissionFilter implements Filter {
//    private static final String USER_ROLE_SESSION_ATTRIBUTE = "userRole";
//    private static final String ERROR_REDIRECT_LOCATION = "/controller?command=main_page";

    // stores {@link Role}s in the map: keys are Role, values are command names
    private final Map<Role, Set<AppCommand>> commandsByRoles;

    /**
     * Constructs a new PermissionFilter.
     */
    public PermissionFilter() {
        this.commandsByRoles = new EnumMap<>(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);

        // fetches all the available command
        for (AppCommand command : AppCommand.values()) {

            // fetches from the command roles, which are available for the command and stores in the map
            for (Role allowedRole : command.getAllowedRoles()) {
                Set<AppCommand> commands = commandsByRoles.get(allowedRole);

                // if in the map yet there is a set, fetches this set and add the commant in it.
                // If there is no set for this Role, creates a set, stores it in the map
                // and adds to the set this command
                if (commands == null) {
                    commands = EnumSet.noneOf(AppCommand.class);
                    commandsByRoles.put(allowedRole, commands);
                }
                // second option: to do it with the 'computeIfAbsent'
                // Set<AppCommand> commands
                // = commandsByRoles.computeIfAbsent(allowedRole, k -> EnumSet.noneOf(AppCommand.class));
                commands.add(command);
            }
        }
    }

    // implements the 'chain of responsibility' pattern: each filter does his job and send the request further.
    // checks whether a command is allowed for a user.

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        // transforming the request into httpServletRequest
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final AppCommand command = AppCommand.of(req.getParameter(ApplicationConstants.COMMAND_PARAM_NAME));
        // get from request the session without its creation if is there no session
        final HttpSession session = req.getSession(false);

        // the logic of the filter
        Role currentRole = extractRoleFromSession(session);
        // fetches from the map a set of allowed commands by a role
        final Set<AppCommand> allowedCommands = commandsByRoles.get(currentRole);
        // checks whether the such a command exist. If yes - allow the request go futher
        if (allowedCommands.contains(command)) {
            // does filter: a chain of filters are executed and at the end transfers the control to the servlet
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // the error appears and here it should go to the error page. If there are no rights - redirects to the main.jsp
            ((HttpServletResponse) servletResponse).sendRedirect(ApplicationConstants.ERROR_REDIRECT_LOCATION);
        }
    }

    private Role extractRoleFromSession(HttpSession session) {
        return session != null && session.getAttribute(ApplicationConstants.USER_ROLE_SESSION_ATTRIBUTE) != null
                ? (Role) session.getAttribute(ApplicationConstants.USER_ROLE_SESSION_ATTRIBUTE)
                : UNAUTHORIZED;
    }
}
