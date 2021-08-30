package com.epam.jwd.Conferences.controller;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * This is a controller by MVC architecture. The application uses the 'Command' pattern.
 */
@WebServlet(urlPatterns = "/controller")
public class ApplicationController extends HttpServlet {

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_APPLICATION_CONTROLLER;
//    public static final String ERROR_WITH_THE_MESSAGE_ERROR_MESSAGE = "Error with the message: ";
//    public static final String COMMAND_PARAM_NAME = "command";

    private String message;

    // write all the methods, with which the controller can work
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    // the 'doPost' method the same as the 'doGet', because architecturally the have no differences
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        // it is awaited in the servlet that the command came from 'request.getParameter'
        final String commandName = request.getParameter(ApplicationConstants.COMMAND_PARAM_NAME);

        // now the AppCommand is used to return the command name
        final Command command = Command.withName(commandName);

        /*
          First option:
          The command is executed and the response is received, with which something should be done.
          HttpServletRequest has the method getRequestDispatcher, which returns RequestDispatcher by a path.
          RequestDispatcher has the method forward(ServletRequest request, ServletResponse response),
          which forwards a request to a corresponding URI insider this server and it should be either a servlet
          or a jsp or html file. The only one question on which URI - on the 'String path;, that receives the
          getRequestDispatcher.

          Second option:
          HttpServletResponse has the sendRedirect(String location) (a location, but not a path
          as in the HttpServletRequest), that means an URL. It cleans the pair ServletRequest/ServletResponse
          (cleans the buffer) and redirects URL to Location, that means that it can be redirected on an other site.
          Therefore CommandResponse should answer to the question: is a result a redirect (to the outer source),
          or a forward (to the inner source). Plus to all the option can work it should be a path
          (see interface CommandResponse). In parameters the CommandRequest should be created.

          there is only one place where the CommandResponse is used, that is why it is realised without a realisation
         */
        final CommandResponse resp = command.execute(new CommandRequest() {
            @Override
            public HttpSession createSession() {
                return request.getSession(true);
            }

            @Override
            public Optional<HttpSession> getCurrentSession() {
                return Optional.ofNullable(request.getSession(false));
            }

            @Override
            public void invalidateCurrentSession() {
                final HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
            }

            @Override
            public String getParameter(String name) {
                return request.getParameter(name);
            }

            @Override
            public void setAttribute(String name, Object value) {
                // setting of the attribute, which will be in the RequestScope
                request.setAttribute(name, value);
            }
        });


        /*
        Now something should be done with the response.
        First option:
        HttpServletRequest has the method getServletDispatcher, which receives a path and returns
        an RequestDispatcher object. In that object there are methods forward and include:
        forward(ServletRequest request, ServletResponse response) redirects a request on the corresponding URI (path),
        which receives RequestDispatcher. RequestDispatcher forwards a request to the resource
        (servlet, JSP file, HTML file) inside this server - the source of the application or includes the resource
        in a response. (In our case we use the forward). The resource can be dynamic or static.
        During forwarding URL in a browser does not change. That means if the site are built on the forward it can be so,
         that URL in a browser will not be changed.
        Second option:
        HttpServletResponse has the method sendRedirect(String location) to a URL. This is a location.
        It is different from the first option, because in the first option it is the path inside the server, that is why
        it can not redirect to an other site. Therefore CommandResponse should answer the question
        'whether this result a redirect result or a forward result' and that is why it should have the method
        boolean redirect(); and to have a path 'String path()'.
        */
        try {
            if (resp.isRedirect()) {
                // if from redirect
                response.sendRedirect(resp.getPath());
            } else {
                // otherwise
                final RequestDispatcher requestDispatcher = request.getRequestDispatcher(resp.getPath());
                // and tell to dispatcher to forward
                requestDispatcher.forward(request, response);
            }
        } catch (IOException | ServletException exception) {
            // to lead all the error to the error page
            try {
                response.sendRedirect("/controller?command=error");
            } catch (IOException e) {
                logger.error(ApplicationConstants.ERROR_WITH_THE_MESSAGE_ERROR_MESSAGE + e.getMessage());
            }
        }
        // now the controller will work with everything. If a commandName will be unknown,
        // the controller understand what it should do
    }

    public void destroy() {
    }
}