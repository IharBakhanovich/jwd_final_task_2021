package com.epam.jwd.Conferences.controller;

import com.epam.jwd.Conferences.command.Command;
import com.epam.jwd.Conferences.command.CommandRequest;
import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.constants.ApplicationConstants;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

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

        // the command is executed and the response is received, with which something should be done
        // у HttpServletRequest есть метод getRequestDispatcher, который возвращает RequestDispatcher по пути.
        // В RequestDispatcher есть метод forward(ServletRequest request, ServletResponse response)
        // - который пересылает запрос на соответствующую URI в рамках этого сервера и это должен быть
        // или сервлет или jsp или html file. Вопрос на какую URI?
        // getRequestDispatcher принимает String path.
        // Второй способ: в HttpServletResponse есть метод sendRedirect(String location) (именно location, а не путь,
        // как в HttpServletRequest), т.е. полноценный URL. Он почистит пару ServletRequest/ServletResponse
        // (очистит буфер) и перешлет URL на location, причем можно заредиректиться на другой сайт.
        // Следовательно CommandResponse должен отвечать на вопрос: является ли результат редиректом (на внешний ресурс),
        // или форвардом - на внутренний ресурс. И еще у него должен быть путь (смотри interface CommandResponse).
        // в параметрах создаем анонимный класс CommandRequest
        // there is only one place where the CommandResponse is used, that is why it is realised without a realisation
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

        //теперь на основании response мы должны что-то делать.
        // 1 способ
        // У HttpServletRequest есть метод getServletDispatcher,
        // который принимает путь(path) и возвращает объект RequestDispatcher. В этом объекте есть методы forward и include.
        // forward(ServletRequest request, ServletResponse response) и пересылает запрос на соответствующую uri (путь).
        // Вопрос на какую uri? А на ту, которую принимает RequestDispatcher (path).
        // RequestDispatcher forwards a request to the resource (servlet, JSP file, HTML file в рамках этого сервера
        // - это ресурс приложения) or includes the resource in a response. В нашем случае нас интересует forward.
        // The resource can be dynamic or static. При форварде url в браузере не меняется. Т.е. фактически
        // если весь сайт построить на форварде можно сделать так, что url в браузере не будет меняться ни при каких условиях.
        // 2 способ
        // В HttpServletResponse есть метод sendRedirect(String location) - на какой-то url. Это location,
        // отличается от прошлого способа, т.к. там был путь в рамках сервера. Т.е. он перешлет url,
        // а значит мы можем заредиректиться на другой сайт.
        // Отсюда следует, что CommandResponse должен отвечать на вопрос: является ли результат редиректа или форварда.
        // т.е. иметь метод boolean redirect(); и у него должен быть путь String path();
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