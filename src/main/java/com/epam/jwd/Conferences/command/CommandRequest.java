package com.epam.jwd.Conferences.command;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * The interface to implement the command requests.
 */
public interface CommandRequest {

    /**
     * у request есть 2 метода:
     * getSession(boolean create) - если create true, то создает новую сессию, если такой не было.
     *                              если create false - возвращает null, если текущей сессии нет.
     * getSession()               - тоже самое, что getSession(true).
     *
     */
    HttpSession createSession();

    Optional<HttpSession> getCurrentSession();

    void invalidateCurrentSession();

    //to reach parameters, which come from the frontend
    String getParameter(String name);

    void setAttribute(String name, Object value);
}
