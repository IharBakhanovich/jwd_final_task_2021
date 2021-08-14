package com.epam.jwd.Conferences.constants;

import com.epam.jwd.Conferences.command.CommandResponse;
import com.epam.jwd.Conferences.command.action.*;
import com.epam.jwd.Conferences.dao.CommonDAO;
import com.epam.jwd.Conferences.dao.DBConferenceDAO;
import com.epam.jwd.Conferences.dao.DBSectionDAO;
import com.epam.jwd.Conferences.dao.JdbcUserDAO;
import com.epam.jwd.Conferences.listener.ApplicationLifecycleListener;
import com.epam.jwd.Conferences.pool.AppConnectionPool;
import com.epam.jwd.Conferences.system.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Stores all the application constants
 */
public class ApplicationConstants {

    /*
     * the private default constructor, to not create the instance of the class with 'new' outside the class.
     */
    private ApplicationConstants() {}

    // from all the classes
    public static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    public static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    public static final String ERROR_ATTRIBUTE_NAME = "error";
    public static final String USERS_ATTRIBUTE_NAME = "users";
    public static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    public static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    public static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    public static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    public static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    public static final String DUPLICATE_SECTION_MESSAGE
            = "The section with such a section name title already exist in the system."
            + " Please choose an other section name.";
    public static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    public static final int MAX_LENGTH_OF_CONFERENCE_TITLE_IN_DB = 30;
    public static final String REPORT_TEXT_PARAMETER_NAME = "reportText";
    public static final String REPORT_TYPE_PARAMETER_NAME = "reportType";
    public static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    public static final String REPORTS_ATTRIBUTE_NAME = "reports";
    public static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    public static final String SECTION_ID_PARAMETER_NAME = "sectionId";
    public static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
    public static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    public static final String SECTION_MANAGER_ID_ATTRIBUTE_NAME = "sectionManagerId";
    public static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;
    public static final String MANAGER_SECTION_NICKNAME_PARAMETER_NAME = "managerSectNickname";
    public static final String CONFERENCE_MANAGER_ID_PARAMETER_NAME = "conferenceManagerId";
    public static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_TO_SECTIONS = "conferenceManager";
    public static final CommandResponse SECTION_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");
    public static final CommandResponse SECTION_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    public static final String ID_PARAMETER_NAME = "id";
    public static final String NICKNAME_PARAMETER_NAME = "nickname";
    public static final String UPDATER_ID_PARAMETER_NAME = "updaterId";
    public static final String UPDATER_ROLE_PARAMETER_NAME = "updaterRole";
    public static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
    public static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";
    public static final String REPORT_ATTRIBUTE_NAME = "report";
    public static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    public static final String MANAGER_CONFERENCE_ATTRIBUTE_NAME = "managerConf";
    public static final String USER_ATTRIBUTE_NAME = "user";

    public static final String ID_COLUMN = "id";
    public static final String CONFERENCE_ID_COLUMN = "conferenceId";

    public static final String USER_ROLE_SESSION_ATTRIBUTE = "userRole";

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$";
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    public static final String EMAIL_REGEX = "^(.+)@(.+)$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public static final String MANAGER_CONF_PARAMETER_NAME = "managerConf";
    public static final String QUESTION_REPORT_ID_PARAMETER_NAME = "questionReportId";

    public static final String QUESTION_TEXT_PARAMETER_NAME = "questionText";
    public static final String QUESTION_REPORT_ID_ATTRIBUTE_NAME = "questionId";
    public static final String QUESTION_TEXT_ATTRIBUTE_NAME = "questionText";

    public static final String QUESTIONS_ATTRIBUTE_NAME = "questions";
    public static final String MANAGER_ID_PARAMETER_NAME = "managerId";
    public static final String MANAGER_ID_ATTRIBUTE_NAME = "managerId";
    public static final CommandResponse SHOW_QUESTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/questions.jsp");

    public static final String MANAGER_SECT_COLUMN = "managerSect";
    public static final String TABLE_NAME_SECTIONS = "sections";

    public static final String INVALID_CONFERENCE_TITLE_TEXT_MSG = "ConferenceTitleShouldNotBeEmptyOrContainOnlySpacesMSG";
    public static final String INVALID_CONFERENCE_TITLE_TEXT_NOT_UTF8_MSG = "ConferenceTitleShouldContainOnlyLatinLettersMSG";
    public static final String INVALID_CONFERENCE_TITLE_TEXT_TOO_LONG_MSG = "ConferenceTitleIsTooLongMSG";

    public static final String INVALID_SECTION_NAME_MSG = "SectionNameShouldNotBeEmptyMSG";
    public static final String INVALID_SECTION_NAME_NOT_UTF8_MSG = "SectionNameShouldContainOnlyLatinSignsMSG";

    public static final String INVALID_SECTION_NAME_TOO_LONG_MSG = "SectionNameIsTooLongMSG";
    public static final String INVALID_FIRST_NAME_IS_TOO_LONG_MSG = "FirstNameIsTooLongMSG";
    public static final String INVALID_REPORT_TEXT_MSG = "ReportTextShouldNotBeEmptyMSG";
    public static final String INVALID_REPORT_TEXT_NOT_UTF8_MSG = "ReportTextShouldContainOnlyLatinSignsMSG";
    public static final String INVALID_PARAMETERS_SOMETHING_WRONG_WITH_PARAMETERS_MSG = "SomethingWrongWithParameters";

    public static final CommandResponse SHOW_APPLICATIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/applications.jsp");
    public static final String APPLICATIONS_ATTRIBUTE_NAME = "applications";
    public static final CommandResponse SHOW_APPLICATIONS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final String MANAGER_ROLE_PARAMETER_NAME = "managerRole";
    public static final String APPLICATION_TOKEN_PARAMETER_NAME = "applicationToken";
    public static final String ALLOWED_REPORT_TYPES_ATTRIBUTE_NAME = "allowedReportTypes";
    public static final String ADMIN_CONSTANT = "ADMIN";
    public static final String APPLICANT_APPLICATION_APPLICATION_TOKEN_VALUE = "applicantApplication";
    public static final String USER_APPLICATION_APPLICATION_TOKEN_VALUE = "userApplication";

    // from class CreateConference
    public static final Logger LOGGER_FOR_CREATE_CONFERENCE = LogManager.getLogger(CreateConference.class);
    public static final CommandResponse CREATE_NEW_CONFERENCE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
    public static final CommandResponse CONFERENCE_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final String DUPLICATE_CONFERENCE_MESSAGE
            = "The conference with such a conference title already exist in the system. Please choose an other conference title.";
    public static final String NO_PERMISSION_TO_CREATE_CONFERENCE_MSG = "YouHaveNoPermissionToCreateAConferenceMSG";
    public static final String INVALID_MANAGER_THERE_IS_NO_SUCH_MANAGER_IN_SYSTEM_MSG = "ThereIsNoSuchManagerInSystemMSG";
    public static final CommandResponse CREATE_CONFERENCE_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class CreateNewUser
    public static final Logger LOGGER_FOR_CREATE_NEW_USER = LogManager.getLogger(CreateNewUser.class);
    public static final String PASSWORD_PARAMETER_NAME = "password";
    public static final String PASSWORD_REPEAT_PARAMETER_NAME = "passwordRepeat";
    public static final CommandResponse CREATE_NEW_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");
    public static final CommandResponse USER_CREATION_SUCCESS_RESPONSE_ADMIN
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    public static final CommandResponse USER_CREATION_SUCCESS_RESPONSE_UNAUTHORISED
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");
    public static final String DUPLICATE_USER_MESSAGE = "The user with such a nickname already exists in the system.";
    public static final int MAX_LENGTH_OF_NICKNAME_IN_DB = 30;
    public static final String INVALID_NICKNAME_IS_EMPTY_MSG = "NicknameShouldNotBeEmptyMSG";
    public static final String INVALID_NICKNAME_NOT_UTF8_MSG = "NicknameShouldContainOnlyLatinSignsMSG";
    public static final String INVALID_PASSWORD_IS_EMPTY_MSG = "PasswordShouldNotBeEmptyMSG";
    public static final String INVALID_PASSWORD_NOT_UTF8_MSG = "PasswordShouldContainOnlyLatinSignsMSG";
    public static final String INVALID_NICKNAME_IS_ALREADY_EXIST_IN_SYSTEM_AND_PASSWORD_REPEATED_WRONG_MSG = "NicknameIsAlreadyExistInSystemAndPasswordRepeatedWrongMSG";
    public static final String INVALID_NICKNAME_SUCH_USER_EXISTS_IN_SYSTEM_MSG = "NicknameIsAlreadyExistInSystemMSG";
    public static final String INVALID_PASSWORD_REPEATED_WRONG_MSG = "PasswordRepeatedWrongMSG";
    public static final String INVALID_PASSWORD_TOO_LONG_MSG = "PasswordTooLongMSG";
    public static final String NO_PERMISSION_TO_CREATE_USER_IN_SYSTEM_MSG = "YouHaveNoPermissionToCreateUserInSystemMSG";
    public static final CommandResponse SHOW_CREATE_NEW_USER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final int MAX_LENGTH_OF_PASSWORD = 32;

    // from class CreateReport
    public static final Logger LOGGER_FOR_CREATE_REPORT = LogManager.getLogger(CreateReport.class);
    public static final String APPLICANT_NICKNAME_PARAMETER_NAME = "applicantNickname";
    public static final CommandResponse CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_REPORT_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");
    public static final CommandResponse CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_ANSWER_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
    public static final CommandResponse REPORT_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    public static final CommandResponse CREATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    //from class CreateSection
    public static final Logger LOGGER_FOR_CREATE_SECTION = LogManager.getLogger(CreateSection.class);
    public static final String MANAGER_SECTION_PARAMETER_NAME = "managerSect";
    public static final CommandResponse CREATE_NEW_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    public static final CommandResponse CREATE_SECTION_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    public static final CommandResponse SECTION_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    public static final String CONFERENCE_MANAGER_ATTRIBUTE_NAME = "conferenceManager";

    // from class UpdateConference
    public static final Logger LOGGER_FOR_UPDATE_CONFERENCE = LogManager.getLogger(UpdateConference.class);
    public static final CommandResponse CONFERENCE_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final CommandResponse CONFERENCE_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");
    public static final String NO_PERMISSION_TO_CREATE_SECTION_MSG = "YouHaveNoPermissionToCreateASectionMSG";
    public static final String NO_PERMISSION_TO_UPDATE_CONFERENCE_MSG = "YouHaveNoPermissionToUpdateAConferenceMSG";
    public static final CommandResponse CREATE_UPDATE_CONFERENCE_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");


    // from class UpdateReport
    public static final String APPLICANT_PARAMETER_NAME = "applicant";
    public static final CommandResponse UPDATE_REPORT_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    public static final String NO_PERMISSION_TO_UPDATE_REPORT_MSG = "YouHaveNoPermissionToUpdateThisReportMSG";
    public static final CommandResponse UPDATE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final String SECTION_NAME_VALUE_FOR_APPLICATIONS_PAGE = "applicantApplications";

    // from class UpdateSection
    public static final String NO_PERMISSION_TO_UPDATE_SECTION_MSG = "YouHaveNoPermissionToUpdateASectionMSG";

    // from class UpdateUser
    public static final String EMAIL_PARAMETER_NAME = "email";
    public static final String FIRSTNAME_PARAMETER_NAME = "firstName";
    public static final String SURNAME_PARAMETER_NAME = "surname";
    public static final String ROLE_PARAMETER_NAME = "role";
    public static final int MAX_LENGTH_OF_EMAIL_IN_DB = 320;
    public static final int MAX_LENGTH_OF_FIRST_NAME_IN_DB = 30;
    public static final int MAX_LENGTH_OF_SURNAME_IN_DB = 30;
    public static final CommandResponse UPDATE_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    public static final CommandResponse UPDATE_USER_SUCCESS_RESPONSE_FOR_ADMIN
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    public static final CommandResponse UPDATE_USER_SUCCESS_RESPONSE_FOR_USER
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    public static final String NO_PERMISSION_TO_UPDATE_USER_MSG = "YouHaveNoPermissionToUpdateUserMSG";
    public static final String USER_TO_UPDATE_DONT_MATCH_WITH_ITS_ID_MSG = "UserToUpdateDoNotMatchWithItsIdMSG";
    public static final String EMAIL_NOT_VALID_MSG = "EmailIsNotValidMSG";
    public static final String EMAIL_NOT_VALID_UTF8_MSG = "EmailShouldContainOnlyLatinSignsMSG";
    public static final String EMAIL_NOT_VALID_TOO_LONG_MSG = "EmailIsTooLongMSG";
    public static final String INVALID_SURNAME_NOT_UTF8_MSG = "SurnameShouldContainOnlyLatinSignsMSG";
    public static final String INVALID_SURNAME_TOO_LONG_MSG = "SurnameIsTooLongMSG";
    public static final String INVALID_FIRST_NAME_NOT_UTF8_MSG = "FirstNameShouldContainOnlyLatinSignsMSG";
    public static final CommandResponse UPDATE_USER_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final String INVALID_ID_NO_SUCH_USER_IN_SYSTEM_MSG = "ThereIsNoSuchAUserInSystemMSG";
    public static final String INVALID_PARAMETERS_MSG = "SomethingWentWrongWithParametersMSG";
    public static final String INVALID_ROLE_PARAMETER_MSG = "SomethingWentWrongWithRoleParameterMSG";
    public static final String NO_PERMISSION_TO_CHANGE_ROLE_TO_ADMIN_MSG = "YouHaveNoPermissionToChaneRoleToAdministratorMSG";
    public static final CommandResponse UPDATE_SECTION_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class LoginCommand
    public static final String LOGIN_PARAM_NAME = "login";
    public static final String PASSWORD_PARAM_NAME = "password";
    public static final CommandResponse LOGIN_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");
    // редиректаемся на index.jsp, после чего должен произойти get
    public static final CommandResponse LOGIN_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(true, "index.jsp");
    public static final String USER_NAME_SESSION_ATTRIBUTE = "userName";
    public static final String USER_ID_SESSION_ATTRIBUTE = "userId";

    // from class LogoutCommand
    public static final CommandResponse MAIN_PAGE_REDIRECT
            = CommandResponse.getCommandResponse(true, "index.jsp");

    // from class ShowConferenceSectionsPage
    public static final CommandResponse SHOW_SECTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    public static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_F_SCSP = "conferenceManager";
    public static final CommandResponse SHOW_CONFERENCE_SECTIONS_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowCreateConferencePage
    public static final CommandResponse CREATE_CONFERENCE_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
    public static final CommandResponse SHOW_CREATE_CONFERENCE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowCreateNewUserPage
    public static final CommandResponse SHOW_CREATE_NEW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");

    // from class ShowCreateReportPage
    public static final CommandResponse CREATE_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");
    public static final CommandResponse SHOW_CREATE_REPORT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    //from class ShowCreateSectionPage
    public static final CommandResponse CREATE_SECTION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    public static final CommandResponse SHOW_CREATE_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    public static final CommandResponse SHOW_CREATE_SECTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from ShowErrorPage **
    public static final CommandResponse ERROR_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/error.jsp");

    // from class ShowLoginPage **
    public static final CommandResponse LOGIN_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");

    // from class ShowLoginPage
    public static final CommandResponse SHOW_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowReportPage
    public static final CommandResponse SHOW_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    public static final String ID_OF_MANAGER_OF_REPORTS_SECTION_ATTRIBUTE_NAME = "idOfManagerOfReportsSection";
    public static final CommandResponse SHOW_REPORT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowSectionReportsPage
    public static final CommandResponse SHOW_SECTION_REPORTS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowUpdateConferencePage
    public static final CommandResponse UPDATE_CONFERENCE_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");
    public static final CommandResponse SHOW_UPDATE_CONFERENCE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowUpdateSectionPage
    public static final CommandResponse UPDATE_SECTION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");
    public static final CommandResponse SHOW_UPDATE_SECTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowUserPage
    public static final CommandResponse SHOW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    public static final CommandResponse SHOW_USER_PAGE_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowUsersPage
    public static final CommandResponse SHOW_USERS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "WEB-INF/jsp/users.jsp");

    // from class ApplicationController
    public static final String COMMAND_PARAM_NAME = "command";

    // from class CommonDAO
    public static final Logger LOGGER_FOR_COMMON_DAO = LogManager.getLogger(CommonDAO.class);

    public static final String FIND_ALL_SQL_TEMPLATE = "select * from %s";
    public static final String FIND_BY_ID_SQL_TEMPLATE = "select * from %s where id = ?";
    public static final String DELETE_ENTITY_BY_ID_SQL_TEMPLATE = "delete from %s where id = ?";

    public static final Logger LOGGER_FOR_DB_CONFERENCE_DAO = LogManager.getLogger(DBConferenceDAO.class);

    public static final String CONFERENCE_TITLE_COLUMN = "conferenceTitle";
    public static final String MANAGER_CONF_COLUMN = "managerConf";
    public static final String TABLE_NAME_CONFERENCES = "conferences";
    public static final String[] COLUMN_NAMES_FOR_DB_CONFERENCE_DAO = {ID_COLUMN, CONFERENCE_TITLE_COLUMN, MANAGER_CONF_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_CONFERENCE_DAO = "select * from %s where %s = ?";

    // from class DBReportDAO
    public static final String SECTION_ID_COLUMN = "sectionId";

    public static final String REPORT_TEXT_COLUMN = "reportText";
    public static final String REPORT_TYPE_COLUMN = "reportType";
    public static final String APPLICANT_COLUMN = "applicant";
    public static final String TABLE_NAME_REPORTS = "reports";
    public static final String QUESTION_REPORT_ID_COLUMN = "questionReportId";
    public static final String[] REPORT_TABLE_COLUMN_NAMES
            = {ID_COLUMN, SECTION_ID_COLUMN, CONFERENCE_ID_COLUMN,
            REPORT_TEXT_COLUMN, REPORT_TYPE_COLUMN, APPLICANT_COLUMN, QUESTION_REPORT_ID_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL
            = "select * from %s where %s = ?";
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_ONE_AND_COLUMN_TWO_FOR_DB_REPORT_DAO_SQL
            = "select * from %s where %s = ? and %s = ?";
    public static final String SELECT_ALL_QUESTION_BY_MANAGER_ID_SQL =
            "select %s.* from %s, %s where %s.%s = 1 and %s.%s = %s.%s and %s.%s = ?";
    public static final String SELECT_ALL_QUESTION_BY_APPLICANT_ID_SQL
            = "select * from %s where %s = 1 and %s = ?";

    // from class DBSectionDAO
    public static final Logger LOGGER_FOR_DB_SECTION_DAO = LogManager.getLogger(DBSectionDAO.class);
    public static final String SECTION_NAME_COLUMN = "sectionName";
    public static final String[] SECTION_TABLE_COLUMN_NAMES
            = {ID_COLUMN, CONFERENCE_ID_COLUMN, SECTION_NAME_COLUMN, MANAGER_SECT_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO = "select * from %s where %s = ?";

    // from class DBUserDAO
    public static final Logger LOGGER_FOR_DB_USER_DAO = LogManager.getLogger(JdbcUserDAO.class);

    public static final String EMAIL_COLUMN = "email";
    public static final String PASSWORD_COLUMN = "password";
    public static final String SALT_COLUMN = "salt";
    public static final String NUMBER_LOGIN_ATTEMPTS_COLUMN = "numberLoginAttempts";
    public static final String VERIFICATION_TOKEN_COLUMN = "verificationToken";
    public static final String EMAIL_VERIFIED_COLUMN = "emailVerified";
    public static final String NICK_NAME_COLUMN = "nickname";
    public static final String FIRST_NAME_COLUMN = "firstName";
    public static final String SURNAME_COLUMN = "surname";
    public static final String ROLE_COLUMN = "role";

    public static final String TABLE_NAME = "users";
    public static final String[] USER_TABLE_COLUMN_NAMES
            = {ID_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN,
            SALT_COLUMN, NUMBER_LOGIN_ATTEMPTS_COLUMN, VERIFICATION_TOKEN_COLUMN,
            EMAIL_VERIFIED_COLUMN, NICK_NAME_COLUMN, FIRST_NAME_COLUMN,
            SURNAME_COLUMN, ROLE_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_USER_DAO = "select * from %s where %s = ?";

    // from class PermissionFilter
    public static final String ERROR_REDIRECT_LOCATION = "/controller?command=main_page";

    // from class ApplicationLifecycleListener
    public static final Logger LOGGER_FOR_APPLICATION_LIFECYCLE_LISTENER
            = LogManager.getLogger(ApplicationLifecycleListener.class);
    public static final String CONFIGPATH = "/config/logger.properties";

    // from class AppUserService
    public static final String NOT_FOUND_MESSAGE = "User with such a login does no exist. Login: %s";

    // from class AppConnectionPool
    public static final Logger LOGGER_FOR_APP_CONNECTION_POOL = LogManager.getLogger(AppConnectionPool.class);
    public static final String DB_MYSQL_PATH = "jdbc:mysql://";
    public static final String SUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE = "ConnectionPool was successfully initialized. Amount of available connections: ";
    public final String UNSUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE = "DB read unsuccessfully. ConnectionPool was NOT initialized";
    public static final int INIT_CONNECTIONS_AMOUNT = 8;
    public static final int CONNECTIONS_GROW_FACTOR = 4;

    // from class Configuration
    public static final Logger LOGGER_FOR_CONFIGURATION = LogManager.getLogger(Configuration.class);
    public static final String FILEPATH
            = "C:\\Studium\\EPAM\\FinalTask_WebProject\\src\\main\\resources\\application.properties";

    // from class AppValidator

    // from class ShowQuestionPage
    public static final CommandResponse SHOW_QUESTION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowCreateAnswerPage
    public static final CommandResponse CREATE_ANSWER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
    public static final String CREATOR_ID_PARAMETER_NAME_FOR_ANSWER = "managerId";
    public static final String CREATOR_ROLE_PARAMETER_NAME_FOR_ANSWER = "managerRole";
    public static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_ANSWER = "questionId";
    public static final CommandResponse SHOW_CREATE_ANSWER_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowQuestionContextPage
    public static final String QUESTION_ID_PARAMETER_NAME_FOR_CONTEXT = "questionIdForContext";
    public static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_CONTEXT = "questionReportIdForContext";
    public static final String QUESTION_TOKEN_NAME = "question";
    public static final String APPLICANT_QUESTIONS_TOKEN_NAME = "applicantQuestions";
    public static final String APPLICANT_QUESTIONS_APPLICATION_TOKEN_VALUE = "applicantQuestions";
    public static final String USER_QUESTIONS_APPLICATION_TOKEN_VALUE = "";
    public static final CommandResponse SHOW_QUESTION_CONTEXT_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowOwnQuestionPage
    public static final String APPLICANT_QUESTIONS_TOKEN = "applicantQuestions";
    public static final CommandResponse SHOW_OWN_QUESTIONS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // Locale's constants
    // class LoginCommand
    public static final String INVALID_CREDENTIALS_MSG = "InvalidCredentialsMSG";

    // class CreateReport

    // class ShowApplicationsPage

    // class ShowOwnApplicationsPage
    public static final CommandResponse SHOW_OWN_APPLICATIONS_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final String APPLICANT_APPLICATIONS_TOKEN = "applicantApplications";

    // class ShowProcessApplicationPage
    public static final CommandResponse PROCESS_APPLICATION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/processApplication.jsp");
    public static final String APPLICATION_ID_PARAMETER_NAME = "applicationId";
    public static final String APPLICATION_TEXT_PARAMETER_NAME = "applicationText";
    public static final String APPLICATION_ID_ATTRIBUTE_NAME = "applicationId";
    public static final String APPLICATION_TOKEN_ATTRIBUTE_NAME = "applicationToken";
    public static final String APPLICATION_TEXT_ATTRIBUTE_NAME = "applicationText";
    public static final String APPLICANT_ATTRIBUTE_NAME = "applicant";
    public static final CommandResponse SHOW_PROCESS_APPLICATION_PAGE_REPORT_ERROR_RESPONSE_TO_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    public static final String MANAGER_CONSTANT = "MANAGER";

}