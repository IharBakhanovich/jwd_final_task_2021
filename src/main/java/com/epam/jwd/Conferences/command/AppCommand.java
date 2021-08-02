package com.epam.jwd.Conferences.command;


import com.epam.jwd.Conferences.command.action.*;
import com.epam.jwd.Conferences.command.authentication.LoginCommand;
import com.epam.jwd.Conferences.command.authentication.LogoutCommand;
import com.epam.jwd.Conferences.command.page.*;
import com.epam.jwd.Conferences.dto.Role;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.Conferences.dto.Role.*;

// для того, чтобы по значению параметра commandName (команде) в контроллере вытащить класс, соответствующий команде
// класс содержит в себе все присутствующие в приложении команды
/**
 * To get the right Command by the 'commandName' parameter in Controller.
 */
public enum AppCommand {
    //соответственно для енама будем передавать параметр (class Command)
    // и в таком духе на каждую команду: название - команда
    MAIN_PAGE(ShowMainPage.getInstance()),
    SHOW_USERS(ShowUsersPage.getInstance(), ADMIN), //посмотреть юзеров может только админ
    SHOW_LOGIN(ShowLoginPage.getInstance(), UNAUTHORIZED), // открывать логин страницу может только UNAUTHORIZED
    LOGOUT(LogoutCommand.getInstance(), USER, ADMIN, MANAGER), //USER и ADMIN могут logout
    LOGIN(LoginCommand.getInstance(), UNAUTHORIZED), //логиниться может только UNAUTHORIZED
    ERROR(ShowErrorPage.getInstance()),//для всех пользователей, поэтому не выделяем,
    SHOW_SECTIONS(ShowConferenceSectionsPage.getInstance(), USER, ADMIN, UNAUTHORIZED, MANAGER),
    SHOW_REPORTS(ShowSectionReportsPage.getInstance(), USER, ADMIN, UNAUTHORIZED, MANAGER),
    SHOW_USER(ShowUserPage.getInstance(), USER, ADMIN, MANAGER), //shows a user
    SHOW_REPORT(ShowReportPage.getInstance(), USER, ADMIN, UNAUTHORIZED, MANAGER), // shows a report.jsp page
    SHOW_CREATE_NEW_USER(ShowCreateNewUserPage.getInstance(), ADMIN, UNAUTHORIZED), // shows createNewUser.jsp page
    SHOW_CREATE_CONFERENCE(ShowCreateConferencePage.getInstance(), ADMIN), // shows createConference.jsp page
    SHOW_CREATE_SECTION(ShowCreateSectionPage.getInstance(), ADMIN, USER, MANAGER), // shows createSection.jsp page
    SHOW_CREATE_REPORT(ShowCreateReportPage.getInstance(), ADMIN, USER, MANAGER), // shows createReport.jsp page
    SHOW_UPDATE_CONFERENCE(ShowUpdateConferencePage.getInstance(), ADMIN), // shows updateConference.jsp page
    SHOW_UPDATE_SECTION(ShowUpdateSectionPage.getInstance(), ADMIN, USER, MANAGER), // shows updateConference.jsp page
    SHOW_QUESTIONS(ShowQuestionPage.getInstance(), ADMIN, MANAGER), // shows question.jsp page
    CREATE_NEW_USER(CreateNewUser.getInstance(), ADMIN, UNAUTHORIZED), // creates new user and users.jsp/login.jsp page
    CREATE_NEW_CONFERENCE(CreateConference.getInstance(), ADMIN), // creates new conference and shows main.jsp page
    CREATE_NEW_SECTION(CreateSection.getInstance(), ADMIN, USER, MANAGER), // creates new section and shows sections.jsp page
    CREATE_NEW_REPORT(CreateReport.getInstance(), ADMIN, USER, MANAGER), // creates new report and shows reports.jsp page
    UPDATE_USER(UpdateUser.getInstance(), ADMIN, USER, MANAGER), // updates user data
    UPDATE_REPORT(UpdateReport.getInstance(), ADMIN, USER, MANAGER), // updates report data
    UPDATE_CONFERENCE(UpdateConference.getInstance(), ADMIN), // updates report data
    UPDATE_SECTION(UpdateSection.getInstance(), ADMIN, USER, MANAGER), // updates report data
    DEFAULT(ShowMainPage.getInstance()); // по дефолту показываем главную страницу

    // поле, которое будет заполняться из конструктора
    private final Command command;

    // поле в котором будут храниться все roles.
    // Соответственно для каждой команды прямо в appCommand можно сказать какие роли имеют право эту команду выполнить
    private final List<Role> allowedRoles;

    // принимаем roles в качестве var args, т.е. можно вообще не передать никаких ролей
    AppCommand(Command command, Role... roles) {
        this.command = command;
        // если ролей нет, до добавляем всех и это значит, что делать можно всем
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : Role.valuesAsList();
    }

    /**
     * Returns the command.
     *
     * @return a {@link Command}.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Returns a List of allowed Roles for the command.
     *
     * @return a {@link List} of allowed {@link Role}s of the command.
     */
    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    //теперь нужен способ по названию получать команду (статический фабричный метод)
    // если имя константы в нижнем регистре совпадает с name, тогда возвращаем эту команду
    // в случае, если такой команды не нашлось - можем возвращать страницу 404 или дефолтную команду
    //
    /**
     * Returns the {@link AppCommand} by the name of the command.
     *
     * @param name ist the name of the command.
     * @return the {@link AppCommand} in case if the command exists in the class.
     *         Otherwise (if the command does not exist in the class) returns DEFAULT {@link AppCommand}.
     */
    public static AppCommand of(String name) {
        // итерируемся по всем присутствующим в приложении командам
        for (AppCommand command : values()) {
            if (command.name().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return DEFAULT;
    }
}
