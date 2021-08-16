package com.epam.jwd.Conferences.command;


import com.epam.jwd.Conferences.command.action.*;
import com.epam.jwd.Conferences.command.authentication.LoginCommand;
import com.epam.jwd.Conferences.command.authentication.LogoutCommand;
import com.epam.jwd.Conferences.command.page.*;
import com.epam.jwd.Conferences.dto.Role;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.Conferences.dto.Role.*;

/**
 * To fetch the right Command by the 'commandName' parameter in Controller.
 * The Class contains all used in system commands.
 *
 * @author Ihar Bakhanovich
 */
public enum AppCommand {

    // for all enums will be the own parameter - command (class Command)
    MAIN_PAGE(ShowMainPage.getInstance()),
    SHOW_USERS(ShowUsersPage.getInstance(), ADMIN), // to show users page can only admin
    SHOW_LOGIN(ShowLoginPage.getInstance(), UNAUTHORIZED), // this page can open only UNAUTHORIZED user
    LOGOUT(LogoutCommand.getInstance(), USER, ADMIN, MANAGER), //USER, ADMIN and MANAGER can logout
    LOGIN(LoginCommand.getInstance(), UNAUTHORIZED), // login command can only UNAUTHORIZED user
    ERROR(ShowErrorPage.getInstance()),// for all users, that is why no roles are specified
    SHOW_SECTIONS(ShowConferenceSectionsPage.getInstance(), USER, ADMIN, UNAUTHORIZED, MANAGER),
    SHOW_REPORTS(ShowSectionReportsPage.getInstance(), USER, ADMIN, UNAUTHORIZED, MANAGER),
    SHOW_USER(ShowUserPage.getInstance(), USER, ADMIN, MANAGER), //shows a user
    SHOW_REPORT(ShowReportPage.getInstance(), USER, ADMIN, UNAUTHORIZED, MANAGER), // shows a report.jsp page
    SHOW_CREATE_NEW_USER(ShowCreateNewUserPage.getInstance(), ADMIN, UNAUTHORIZED), // shows createNewUser.jsp page
    SHOW_CREATE_CONFERENCE(ShowCreateConferencePage.getInstance(), ADMIN), // shows createConference.jsp page
    SHOW_CREATE_SECTION(ShowCreateSectionPage.getInstance(), ADMIN, USER, MANAGER), // shows createSection.jsp page
    SHOW_CREATE_REPORT(ShowCreateReportPage.getInstance(), ADMIN, USER, MANAGER), // shows createReport.jsp page
    SHOW_CREATE_ANSWER(ShowCreateAnswerPage.getInstance(), ADMIN, USER, MANAGER), // shows createReport.jsp page
    SHOW_UPDATE_CONFERENCE(ShowUpdateConferencePage.getInstance(), ADMIN), // shows updateConference.jsp page
    SHOW_UPDATE_SECTION(ShowUpdateSectionPage.getInstance(), ADMIN, USER, MANAGER), // shows updateConference.jsp page
    SHOW_QUESTIONS(ShowQuestionPage.getInstance(), ADMIN, MANAGER), // shows question.jsp page
    SHOW_APPLICATIONS(ShowApplicationsPage.getInstance(), ADMIN, MANAGER), // shows applications.jsp page
    SHOW_OWN_QUESTIONS(ShowOwnQuestionPage.getInstance(), ADMIN, MANAGER, USER), // shows question.jsp page
    SHOW_OWN_APPLICATIONS(ShowOwnApplicationsPage.getInstance(), ADMIN, MANAGER, USER), // shows applications.jsp page
    SHOW_PROCESS_APPLICATION(ShowProcessApplicationPage.getInstance(), ADMIN, MANAGER, USER), // shows processApplications.jsp page
    SHOW_HELP(ShowHelpPage.getInstance()), // shows help.jsp page
    SHOW_QUESTION_CONTEXT(ShowQuestionContextPage.getInstance(), ADMIN, MANAGER), // shows reports.jsp page that includes only question and all the answers
    CREATE_NEW_USER(CreateNewUser.getInstance(), ADMIN, UNAUTHORIZED), // creates new user and users.jsp/login.jsp page
    CREATE_NEW_CONFERENCE(CreateConference.getInstance(), ADMIN), // creates new conference and shows main.jsp page
    CREATE_NEW_SECTION(CreateSection.getInstance(), ADMIN, USER, MANAGER), // creates new section and shows sections.jsp page
    CREATE_NEW_REPORT(CreateReport.getInstance(), ADMIN, USER, MANAGER), // creates new report and shows reports.jsp page
    UPDATE_USER(UpdateUser.getInstance(), ADMIN, USER, MANAGER), // updates user data
    UPDATE_REPORT(UpdateReport.getInstance(), ADMIN, USER, MANAGER), // updates report data
    UPDATE_CONFERENCE(UpdateConference.getInstance(), ADMIN), // updates report data
    UPDATE_SECTION(UpdateSection.getInstance(), ADMIN, USER, MANAGER), // updates report data
    DEFAULT(ShowMainPage.getInstance()); // show by default main page

    // the field, that filled in from the constructor
    private final Command command;

    // the field, that contains all the roles. Therefore in Controller for all the command can be determined,
    // which roles has rights to invoke this command.
    private final List<Role> allowedRoles;

    // take roles as 'var args', i.e. it can be no roles to be transferred
    AppCommand(Command command, Role... roles) {
        this.command = command;
        // if there are no roles that means that all the roles can invoke this command
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

    /**
     * Returns the {@link AppCommand} by the name of the command. If the name of the constant without cases
     * matches with name, it returns this command/ If there is no such a command - returns DEFAULT command
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