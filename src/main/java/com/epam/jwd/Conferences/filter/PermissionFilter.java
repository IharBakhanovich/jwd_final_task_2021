package com.epam.jwd.Conferences.filter;

import com.epam.jwd.Conferences.command.AppCommand;
import com.epam.jwd.Conferences.dto.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static com.epam.jwd.Conferences.command.authentication.LoginCommand.USER_ROLE_SESSION_ATTRIBUTE;
import static com.epam.jwd.Conferences.controller.ApplicationController.COMMAND_PARAM_NAME;
import static com.epam.jwd.Conferences.dto.Role.UNAUTHORIZED;

// ранее фильтры необходимо было конфигурить через web.xml, тэгом filter,
// внутри которого задавать имя и т.д. Начиная с сервлетов 3-й версии, можно делать это с помощью аннотаций
@WebFilter(urlPatterns = "/*")
public class PermissionFilter implements Filter {
    private static final String ERROR_REDIRECT_LOCATION = "/controller?command=main_page";
    //будем хранить мапу енам, где ключи это енамы (Role), а в качестве значения команда, которую можно выполнить
    private final Map<Role, Set<AppCommand>> commandsByRoles;

    public PermissionFilter() {
        this.commandsByRoles = new EnumMap<>(Role.class);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // у filterConfig есть следующие методы и это может быть удобно, если нужно,
        // например воспользоваться servletContextом
//        filterConfig.getServletContext();
//        filterConfig.getInitParameter();
//        filterConfig.getFilterName();
//        filterConfig.getInitParameterNames();
        Filter.super.init(filterConfig);
        // чтобы знать какие команды может выполнять какая роль
        //достаем все команды, которые у нас присутствуют
        for (AppCommand command : AppCommand.values()) {
            //достаем из каждой из них доступные роли, проходимся по ним и раскладываем по этой мапе
            for (Role allowedRole : command.getAllowedRoles()) {
                Set<AppCommand> commands = commandsByRoles.get(allowedRole);
                //если в мапе уже по такой роли кто-то что-то положил в ее сет,
                // то мы достаем этот сет и просто добавляем в него команду.
                // А если по такой роли еще никто ничего не клал,
                // то мы сперва создаем сет и кладем его в мапу,
                // а уже потом добавляем в него команду
                if (commands == null) {
                    commands = EnumSet.noneOf(AppCommand.class);
                    commandsByRoles.put(allowedRole, commands);
                }
                // все это можно было бы сделать с помощью computeIfAbsent
                // Set<AppCommand> commands = commandsByRoles.computeIfAbsent(allowedRole, k -> EnumSet.noneOf(AppCommand.class));
                commands.add(command);
            }
        }
    }

    // это как раз паттерн chain of responsibility - есть несколько фильтров,
// какждый из которых фильтрует по своему назначению
    // нас будет интересовать из request достать команду и проверить, чтобы команда была доступной для роли пользователя
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


        // первым делом request нужно преобразовать в httpServletRequest
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final AppCommand command = AppCommand.of(req.getParameter(COMMAND_PARAM_NAME));
        //затем достаем из request сессию, причем так, чтобы она не создалась, если ее нет
        final HttpSession session = req.getSession(false);
        //далее логика фильтра
        Role currentRole = extractRoleFromSession(session);
        // теперь из мапы по текущей роли мы можем достать сет дозволенных команд
        final Set<AppCommand> allowedCommands = commandsByRoles.get(currentRole);
        // и проверить есть ли такая команда. И если все хорошо, то мы должны request пропустить
        if (allowedCommands.contains(command)) {
            // как пропустить request? В будущем могут появиться следующие фильтры,
            // следовательно он должен пройти всю цепочку фильтров до конца
            //Поэтому мы говорим цепочке - выполняй фильтер
            // эта строка продолжит цепочку фильтров, если такие есть и в конце концов придет к сервлету (передаст ему выполнение)
            // можно здесь и прервать выполнение
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // появляется error.jsp и здесь мы должны на эту страницу перейти
            ((HttpServletResponse) servletResponse).sendRedirect(ERROR_REDIRECT_LOCATION); //тут заменил command=error на command=main_page, чтобы сбрасывать всех у кого не хватает прав на главную страницу
            // после error можно &errorCode=403, а в ShowErrorPage в методе execute позаполнять эти поля
        }
    }

    private Role extractRoleFromSession(HttpSession session) {
        return session != null && session.getAttribute(USER_ROLE_SESSION_ATTRIBUTE) != null
                ? (Role) session.getAttribute(USER_ROLE_SESSION_ATTRIBUTE)
                : UNAUTHORIZED;
    }
}
