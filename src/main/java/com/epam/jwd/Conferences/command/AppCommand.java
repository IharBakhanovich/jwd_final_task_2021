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
public enum AppCommand {
    //соответственно для енама будем передавать параметр (class Command)
    // и в таком духе на каждую команду: название - команда
    MAIN_PAGE(ShowMainPage.getInstance()),
    SHOW_USERS(ShowUsersPage.getInstance(), ADMIN), //посмотреть юзеров может только админ
    SHOW_LOGIN(ShowLoginPage.getInstance(), UNAUTHORIZED), // открывать логин страницу может только UNAUTHORIZED
    LOGOUT(LogoutCommand.getInstance(), USER, ADMIN), //USER и ADMIN могут logout
    LOGIN(LoginCommand.getInstance(), UNAUTHORIZED), //логиниться может только UNAUTHORIZED
    ERROR(ShowErrorPage.getInstance()),//для всех пользователей, поэтому не выделяем,
    SHOW_SECTIONS(ShowConferenceSectionsPage.getInstance(), USER, ADMIN, UNAUTHORIZED),
    SHOW_REPORTS(ShowSectionReportsPage.getInstance(), USER, ADMIN, UNAUTHORIZED),
    SHOW_USER(ShowUserPage.getInstance(), USER, ADMIN), //shows a user
    SHOW_REPORT(ShowReportPage.getInstance(), USER, ADMIN, UNAUTHORIZED), // shows a report
    SHOW_CREATE_NEW_USER(ShowCreateNewUserPage.getInstance(), ADMIN, UNAUTHORIZED), // shows createNewUser.jsp page
    SHOW_CREATE_CONFERENCE(ShowCreateConferencePage.getInstance(), ADMIN), // shows createConference.jsp page
    SHOW_CREATE_SECTION(ShowCreateSectionPage.getInstance(), ADMIN, USER), // shows createSection.jsp page
    SHOW_CREATE_REPORT(ShowCreateReportPage.getInstance(), ADMIN, USER, MANAGER), // shows createReport.jsp page
    CREATE_NEW_USER(CreateNewUser.getInstance(), ADMIN, UNAUTHORIZED), // creates new user and users.jsp/login.jsp page
    CREATE_NEW_CONFERENCE(CreateConference.getInstance(), ADMIN), // creates new conference and shows main.jsp page
    CREATE_NEW_SECTION(CreateSection.getInstance(), ADMIN, USER), // creates new section and shows sections.jsp page
    CREATE_NEW_REPORT(CreateReport.getInstance(), ADMIN, USER), // creates new report and shows reports.jsp page
    UPDATE_USER(UpdateUser.getInstance(), ADMIN, USER), // updates user data
    UPDATE_REPORT(UpdateReport.getInstance(), ADMIN, USER), // updates report data
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

    public Command getCommand() {
        return command;
    }

    public List<Role> getAllowedRoles() {
        return allowedRoles;
    }

    //теперь нужен способ по названию получать команду (статический фабричный метод)
    // если имя константы в нижнем регистре совпадает с name, тогда возвращаем эту команду
    // в случае, если такой команды не нашлось - можем возвращать страницу 404 или дефолтную команду
    //
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
