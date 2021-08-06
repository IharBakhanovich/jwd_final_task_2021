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
    private static final String CREATOR_ID_PARAMETER_NAME = "creatorId";
    private static final String CREATOR_ROLE_PARAMETER_NAME = "creatorRole";
    private static final String ERROR_ATTRIBUTE_NAME = "error";
    private static final String USERS_ATTRIBUTE_NAME = "users";
    private static final String CONFERENCE_TITLE_PARAMETER_NAME = "conferenceTitle";
    private static final String CONFERENCE_ID_PARAMETER_NAME = "conferenceId";
    private static final String SECTION_NAME_PARAMETER_NAME = "sectionName";
    private static final String CONFERENCE_TITLE_ATTRIBUTE_NAME = "conferenceTitle";
    private static final String CONFERENCE_ID_ATTRIBUTE_NAME = "conferenceId";
    private static final String DUPLICATE_SECTION_MESSAGE
            = "The section with such a section name title already exist in the system."
            + " Please choose an other section name.";
    private static final String CONFERENCES_ATTRIBUTE_NAME = "conferences";
    private static final int MAX_LENGTH_OF_CONFERENCE_TITLE_IN_DB = 30;
    private static final String REPORT_TEXT_PARAMETER_NAME = "reportText";
    private static final String REPORT_TYPE_PARAMETER_NAME = "reportType";
    private static final String SECTIONS_ATTRIBUTE_NAME = "sections";
    private static final String REPORTS_ATTRIBUTE_NAME = "reports";
    private static final String SECTION_NAME_ATTRIBUTE_NAME = "sectionName";
    private static final String SECTION_ID_PARAMETER_NAME = "sectionId";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME = "conferenceManagerId";
    private static final String SECTION_ID_ATTRIBUTE_NAME = "sectionId";
    private static final String SECTION_MANAGER_ID_ATTRIBUTE_NAME = "sectionManagerId";
    private static final int MAX_LENGTH_OF_SECTION_NAME_IN_DB = 90;
    private static final String MANAGER_SECTION_NICKNAME_PARAMETER_NAME = "managerSectNickname";
    private static final String CONFERENCE_MANAGER_ID_PARAMETER_NAME = "conferenceManagerId";
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_TO_SECTIONS = "conferenceManager";
    private static final CommandResponse SECTION_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");
    private static final CommandResponse SECTION_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    private static final String ID_PARAMETER_NAME = "id";
    private static final String NICKNAME_PARAMETER_NAME = "nickname";
    private static final String UPDATER_ID_PARAMETER_NAME = "updaterId";
    private static final String UPDATER_ROLE_PARAMETER_NAME = "updaterRole";
    private static final String CREATOR_ID_ATTRIBUTE_NAME = "creatorId";
    private static final String CREATOR_ROLE_ATTRIBUTE_NAME = "creatorRole";
    private static final String REPORT_ATTRIBUTE_NAME = "report";
    private static final CommandResponse SHOW_REPORTS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");
    private static final String MANAGER_CONFERENCE_ATTRIBUTE_NAME = "managerConf";
    private static final String USER_ATTRIBUTE_NAME = "user";

    private static final String ID_COLUMN = "id";
    private static final String CONFERENCE_ID_COLUMN = "conferenceId";

    private static final String USER_ROLE_SESSION_ATTRIBUTE = "userRole";

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String MANAGER_CONF_PARAMETER_NAME = "managerConf";
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME = "questionReportId";

    private static final String QUESTION_TEXT_PARAMETER_NAME = "questionText";
    private static final String QUESTION_REPORT_ID_ATTRIBUTE_NAME = "questionId";
    private static final String QUESTION_TEXT_ATTRIBUTE_NAME = "questionText";

    public static final String QUESTIONS_ATTRIBUTE_NAME = "questions";
    public static final String MANAGER_ID_PARAMETER_NAME = "managerId";
    public static final String MANAGER_ID_ATTRIBUTE_NAME = "managerId";
    private static final CommandResponse SHOW_QUESTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/questions.jsp");

    private static final String MANAGER_SECT_COLUMN = "managerSect";
    private static final String TABLE_NAME_SECTIONS = "sections";

    private static final String INVALID_CONFERENCE_TITLE_TEXT_MSG = "ConferenceTitleShouldNotBeEmptyOrContainOnlySpacesMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_NOT_UTF8_MSG = "ConferenceTitleShouldContainOnlyLatinLettersMSG";
    private static final String INVALID_CONFERENCE_TITLE_TEXT_TOO_LONG_MSG = "ConferenceTitleIsTooLongMSG";

    private static final String INVALID_SECTION_NAME_MSG = "SectionNameShouldNotBeEmptyMSG";
    private static final String INVALID_SECTION_NAME_NOT_UTF8_MSG = "SectionNameShouldContainOnlyLatinSignsMSG";

    private static final String INVALID_SECTION_NAME_TOO_LONG_MSG = "SectionNameIsTooLongMSG";
    private static final String INVALID_FIRST_NAME_IS_TOO_LONG_MSG = "FirstNameIsTooLongMSG";
    private static final String INVALID_REPORT_TEXT_MSG = "ReportTextShouldNotBeEmptyMSG";
    private static final String INVALID_REPORT_TEXT_NOT_UTF8_MSG = "ReportTextShouldContainOnlyLatinSignsMSG";

    // from class CreateConference
    private static final Logger LOGGER_FOR_CREATE_CONFERENCE = LogManager.getLogger(CreateConference.class);
    private static final CommandResponse CREATE_NEW_CONFERENCE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");
    private static final CommandResponse CONFERENCE_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final String DUPLICATE_CONFERENCE_MESSAGE
            = "The conference with such a conference title already exist in the system. Please choose an other conference title.";
    private static final String NO_PERMISSION_TO_CREATE_CONFERENCE_MSG = "YouHaveNoPermissionToCreateAConferenceMSG";

    // from class CreateNewUser
    private static final Logger LOGGER_FOR_CREATE_NEW_USER = LogManager.getLogger(CreateNewUser.class);
    private static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String PASSWORD_REPEAT_PARAMETER_NAME = "passwordRepeat";
    private static final CommandResponse CREATE_NEW_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");
    private static final CommandResponse USER_CREATION_SUCCESS_RESPONSE_ADMIN
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    private static final CommandResponse USER_CREATION_SUCCESS_RESPONSE_UNAUTHORISED
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");
    private static final String DUPLICATE_USER_MESSAGE = "The user with such a nickname already exists in the system.";
    private static final int MAX_LENGTH_OF_NICKNAME_IN_DB = 30;
    private static final String INVALID_NICKNAME_IS_EMPTY_MSG = "NicknameShouldNotBeEmptyMSG";
    private static final String INVALID_NICKNAME_NOT_UTF8_MSG = "NicknameShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_PASSWORD_IS_EMPTY_MSG = "PasswordShouldNotBeEmptyMSG";
    private static final String INVALID_PASSWORD_NOT_UTF8_MSG = "PasswordShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_NICKNAME_IS_ALREADY_EXIST_IN_SYSTEM_AND_PASSWORD_REPEATED_WRONG_MSG = "NicknameIsAlreadyExistInSystemAndPasswordRepeatedWrongMSG";
    private static final String INVALID_NICKNAME_SUCH_USER_EXISTS_IN_SYSTEM_MSG = "NicknameIsAlreadyExistInSystemMSG";
    private static final String INVALID_PASSWORD_REPEATED_WRONG_MSG = "PasswordRepeatedWrongMSG";

    // from class CreateReport
    private static final Logger LOGGER_FOR_CREATE_REPORT = LogManager.getLogger(CreateReport.class);
    private static final String APPLICANT_NICKNAME_PARAMETER_NAME = "applicantNickname";
    private static final CommandResponse CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_REPORT_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");
    private static final CommandResponse CREATE_NEW_REPORT_ERROR_RESPONSE_TO_CREATE_ANSWER_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
    private static final CommandResponse REPORT_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/reports.jsp");


    //from class CreateSection
    private static final Logger LOGGER_FOR_CREATE_SECTION = LogManager.getLogger(CreateSection.class);
    private static final String MANAGER_SECTION_PARAMETER_NAME = "managerSect";
    private static final CommandResponse CREATE_NEW_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");

    private static final CommandResponse SECTION_CREATION_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    private static final String CONFERENCE_MANAGER_ATTRIBUTE_NAME = "conferenceManager";

    // from class UpdateConference
    private static final Logger LOGGER_FOR_UPDATE_CONFERENCE = LogManager.getLogger(UpdateConference.class);
    private static final CommandResponse CONFERENCE_UPDATE_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");
    private static final CommandResponse CONFERENCE_UPDATE_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");
    private static final String NO_PERMISSION_TO_CREATE_SECTION_MSG = "YouHaveNoPermissionToCreateASectionMSG";
    private static final String NO_PERMISSION_TO_UPDATE_CONFERENCE_MSG = "YouHaveNoPermissionToUpdateAConferenceMSG";


    // from class UpdateReport
    private static final String APPLICANT_PARAMETER_NAME = "applicant";
    private static final CommandResponse UPDATE_REPORT_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    private static final String NO_PERMISSION_TO_UPDATE_REPORT_MSG = "YouHaveNoPermissionToUpdateThisReportMSG";

    // from class UpdateSection
    private static final String NO_PERMISSION_TO_UPDATE_SECTION_MSG = "YouHaveNoPermissionToUpdateASectionMSG";

    // from class UpdateUser
    private static final String EMAIL_PARAMETER_NAME = "email";
    private static final String FIRSTNAME_PARAMETER_NAME = "firstName";
    private static final String SURNAME_PARAMETER_NAME = "surname";
    private static final String ROLE_PARAMETER_NAME = "role";
    public static final int MAX_LENGTH_OF_EMAIL_IN_DB = 320;
    public static final int MAX_LENGTH_OF_FIRST_NAME_IN_DB = 30;
    public static final int MAX_LENGTH_OF_SURNAME_IN_DB = 30;
    private static final CommandResponse UPDATE_USER_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    private static final CommandResponse UPDATE_USER_SUCCESS_RESPONSE_FOR_ADMIN
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/users.jsp");
    private static final CommandResponse UPDATE_USER_SUCCESS_RESPONSE_FOR_USER
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");
    private static final String NO_PERMISSION_TO_UPDATE_USER_MSG = "YouHaveNoPermissionToUpdateUserMSG";
    private static final String USER_TO_UPDATE_DONT_MATCH_WITH_ITS_ID_MSG = "UserToUpdateDoNotMatchWithItsIdMSG";
    private static final String EMAIL_NOT_VALID_MSG = "EmailIsNotValidMSG";
    private static final String EMAIL_NOT_VALID_UTF8_MSG = "EmailShouldContainOnlyLatinSignsMSG";
    private static final String EMAIL_NOT_VALID_TOO_LONG_MSG = "EmailIsTooLongMSG";
    private static final String INVALID_SURNAME_NOT_UTF8_MSG = "SurnameShouldContainOnlyLatinSignsMSG";
    private static final String INVALID_SURNAME_TOO_LONG_MSG = "SurnameIsTooLongMSG";
    private static final String INVALID_FIRST_NAME_NOT_UTF8_MSG = "FirstNameShouldContainOnlyLatinSignsMSG";

    // from class LoginCommand
    private static final String LOGIN_PARAM_NAME = "login";
    private static final String PASSWORD_PARAM_NAME = "password";
    private static final CommandResponse LOGIN_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");
    // редиректаемся на index.jsp, после чего должен произойти get
    private static final CommandResponse LOGIN_SUCCESS_RESPONSE
            = CommandResponse.getCommandResponse(true, "index.jsp");
    private static final String USER_NAME_SESSION_ATTRIBUTE = "userName";
    private static final String USER_ID_SESSION_ATTRIBUTE = "userId";

    // from class LogoutCommand
    private static final CommandResponse MAIN_PAGE_REDIRECT
            = CommandResponse.getCommandResponse(true, "index.jsp");

    // from class ShowConferenceSectionsPage
    private static final CommandResponse SHOW_SECTIONS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/sections.jsp");
    private static final String CONFERENCE_MANAGER_ID_ATTRIBUTE_NAME_F_SCSP = "conferenceManager";

    // from class ShowCreateConferencePage
    private static final CommandResponse CREATE_CONFERENCE_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createConference.jsp");

    // from class ShowCreateNewUserPage
    private static final CommandResponse SHOW_CREATE_NEW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createNewUser.jsp");

    // from class ShowCreateReportPage
    private static final CommandResponse CREATE_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createReport.jsp");

    //from class ShowCreateSectionPage
    private static final CommandResponse CREATE_SECTION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");
    private static final CommandResponse SHOW_CREATE_SECTION_ERROR_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createSection.jsp");

    // from ShowErrorPage **
    public static final CommandResponse ERROR_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/error.jsp");

    // from class ShowLoginPage **
    public static final CommandResponse LOGIN_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/login.jsp");

    // from class ShowLoginPage
    private static final CommandResponse SHOW_MAIN_PAGE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/main.jsp");

    // from class ShowReportPage
    private static final CommandResponse SHOW_REPORT_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/report.jsp");
    public static final String ID_OF_MANAGER_OF_REPORTS_SECTION_ATTRIBUTE_NAME = "idOfManagerOfReportsSection";

    // from class ShowSectionReportsPage

    // from class ShowUpdateConferencePage
    private static final CommandResponse UPDATE_CONFERENCE_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateConference.jsp");

    // from class ShowUpdateSectionPage
    private static final CommandResponse UPDATE_SECTION_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/updateSection.jsp");

    // from class ShowUserPage
    private static final CommandResponse SHOW_USER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/user.jsp");

    // from class ShowUsersPage
    private static final CommandResponse SHOW_USERS_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "WEB-INF/jsp/users.jsp");

    // from class ApplicationController
    public static final String COMMAND_PARAM_NAME = "command";

    // from class CommonDAO
    private static final Logger LOGGER_FOR_COMMON_DAO = LogManager.getLogger(CommonDAO.class);

    private static final String FIND_ALL_SQL_TEMPLATE = "select * from %s";
    private static final String FIND_BY_ID_SQL_TEMPLATE = "select * from %s where id = ?";
    private static final String DELETE_ENTITY_BY_ID_SQL_TEMPLATE = "delete from %s where id = ?";

    private static final Logger LOGGER_FOR_DB_CONFERENCE_DAO = LogManager.getLogger(DBConferenceDAO.class);

    private static final String CONFERENCE_TITLE_COLUMN = "conferenceTitle";
    private static final String MANAGER_CONF_COLUMN = "managerConf";
    private static final String TABLE_NAME_CONFERENCES = "conferences";
    private static final String[] COLUMN_NAMES_FOR_DB_CONFERENCE_DAO = {ID_COLUMN, CONFERENCE_TITLE_COLUMN, MANAGER_CONF_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_CONFERENCE_DAO = "select * from %s where %s = ?";

    // from class DBReportDAO
    private static final String SECTION_ID_COLUMN = "sectionId";

    private static final String REPORT_TEXT_COLUMN = "reportText";
    private static final String REPORT_TYPE_COLUMN = "reportType";
    private static final String APPLICANT_COLUMN = "applicant";
    private static final String TABLE_NAME_REPORTS = "reports";
    private static final String QUESTION_REPORT_ID_COLUMN = "questionReportId";
    private static final String[] REPORT_TABLE_COLUMN_NAMES
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
    private static final Logger LOGGER_FOR_DB_SECTION_DAO = LogManager.getLogger(DBSectionDAO.class);
    private static final String SECTION_NAME_COLUMN = "sectionName";
    private static final String[] SECTION_TABLE_COLUMN_NAMES
            = {ID_COLUMN, CONFERENCE_ID_COLUMN, SECTION_NAME_COLUMN, MANAGER_SECT_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO = "select * from %s where %s = ?";

    // from class DBUserDAO
    private static final Logger LOGGER_FOR_DB_USER_DAO = LogManager.getLogger(JdbcUserDAO.class);

    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    private static final String SALT_COLUMN = "salt";
    private static final String NUMBER_LOGIN_ATTEMPTS_COLUMN = "numberLoginAttempts";
    private static final String VERIFICATION_TOKEN_COLUMN = "verificationToken";
    private static final String EMAIL_VERIFIED_COLUMN = "emailVerified";
    private static final String NICK_NAME_COLUMN = "nickname";
    private static final String FIRST_NAME_COLUMN = "firstName";
    private static final String SURNAME_COLUMN = "surname";
    private static final String ROLE_COLUMN = "role";

    private static final String TABLE_NAME = "users";
    private static final String[] USER_TABLE_COLUMN_NAMES
            = {ID_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN,
            SALT_COLUMN, NUMBER_LOGIN_ATTEMPTS_COLUMN, VERIFICATION_TOKEN_COLUMN,
            EMAIL_VERIFIED_COLUMN, NICK_NAME_COLUMN, FIRST_NAME_COLUMN,
            SURNAME_COLUMN, ROLE_COLUMN};
    private static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_USER_DAO = "select * from %s where %s = ?";

    // from class PermissionFilter
    private static final String ERROR_REDIRECT_LOCATION = "/controller?command=main_page";

    // from class ApplicationLifecycleListener
    private static final Logger LOGGER_FOR_APPLICATION_LIFECYCLE_LISTENER
            = LogManager.getLogger(ApplicationLifecycleListener.class);
    private static final String CONFIGPATH = "/config/logger.properties";

    // from class AppUserService
    private static final String NOT_FOUND_MESSAGE = "User with such a login does no exist. Login: %s";

    // from class AppConnectionPool
    private static final Logger LOGGER_FOR_APP_CONNECTION_POOL = LogManager.getLogger(AppConnectionPool.class);
    public static final String DB_MYSQL_PATH = "jdbc:mysql://";
    public static final String SUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE = "ConnectionPool was successfully initialized. Amount of available connections: ";
    private final String UNSUCCESSFULL_CONNECTIONPOOL_INITIALISATION_MESSAGE = "DB read unsuccessfully. ConnectionPool was NOT initialized";
    private static final int INIT_CONNECTIONS_AMOUNT = 8;
    private static final int CONNECTIONS_GROW_FACTOR = 4;

    // from class Configuration
    private static final Logger LOGGER_FOR_CONFIGURATION = LogManager.getLogger(Configuration.class);
    private static final String FILEPATH
            = "C:\\Studium\\EPAM\\FinalTask_WebProject\\src\\main\\resources\\application.properties";

    // from class AppValidator

    // from class ShowQuestionPage

    // from class ShowCreateAnswerPage
    private static final CommandResponse CREATE_ANSWER_PAGE_RESPONSE
            = CommandResponse.getCommandResponse(false, "/WEB-INF/jsp/createAnswer.jsp");
    private static final String CREATOR_ID_PARAMETER_NAME_FOR_ANSWER = "managerId";
    private static final String CREATOR_ROLE_PARAMETER_NAME_FOR_ANSWER = "managerRole";
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_ANSWER = "questionId";

    // from class ShowQuestionContextPage
    private static final String QUESTION_ID_PARAMETER_NAME_FOR_CONTEXT = "questionIdForContext";
    private static final String QUESTION_REPORT_ID_PARAMETER_NAME_FOR_CONTEXT = "questionReportIdForContext";
    private static final String QUESTION_TOKEN_NAME = "question";
    private static final String APPLICANT_QUESTIONS_TOKEN_NAME = "applicantQuestions";

    // from class ShowOwnQuestionPage


    // Locale's constants
    // class LoginCommand
    private static final String INVALID_CREDENTIALS_MSG = "InvalidCredentialsMSG";

    // class CreateReport



}