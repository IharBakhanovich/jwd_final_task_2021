package com.epam.jwd.conferences.validator;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import com.epam.jwd.Conferences.validator.Validator;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Tests for the ReportService
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ValidatorTest {

    public static final String SECOND_SECTION_NAME = "RossiRossiRossi";
    public static final String CONFERENCE_TITLE = "testConference";
    public static final String FIRST_SECTION_NAME = "testSection";
    public static final Long MANAGER_CONFERENCE_ID = 1L;
    public static final String REPORT_APPLICATION_ONE_TEXT = "testApplicationReport";
    public static final String REPORT_QUESTION_TEXT = "testQuestionReport";
    public static final String UPDATED_REPORT_QUESTION_TEXT = "RossiForever";
    public static final String USER_ONE_NICKNAME_FOR_VALIDATOR_TEST = "userOneForValidatorTest";
    public static final String USER_TWO_NICKNAME_FOR_VALIDATOR_TEST = "userTwoForValidatorTest";
    Conference conferenceForTest = new Conference(1L, CONFERENCE_TITLE, MANAGER_CONFERENCE_ID);
    ConferenceDAO conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    Long conferenceId;
    Long sectionOneId;
    String sectionOneNameFromDB;
    Long sectionTwoId;
    String sectionTwoNameFromDatabase;
    UserService service = UserService.retrieve();
    Long newUserOneFromDBId;
    Long newUserTwoFromDBId;
    Long reportApplicationOneId;
    List<Report> allreportsInTheSystemBeforeTests;

    private static final String VALIDE_STRING_TO_VALIDATE = "RossiRossiRossi";
    private static final String INVALIDE_STRING_TO_VALIDATE = "русский текст";
    private static final String VALIDE_EMAIL_TO_VALIDATE = "email@email.de";
    private static final String INVALIDE_EMAIL_TO_VALIDATE = "email.email.de";
    private static final int MAX_LENGTH_FOR_VALIDATOR = 10;
    private static final String INVALIDE_STRING_TO_VALIDATE_FOR_MAX_LENGTH = "RossiRossiRossi";
    private static final String VALIDE_STRING_TO_VALIDATE_FOR_MAX_LENGTH = "RossiRossi";

    private static final Validator validator = Validator.retrieve();

    @BeforeAll
    public void setUp() {
        // initialize connectionPool
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
        // creating the Conference and fetching its id
        try {
            service.createConference(conferenceForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Conference> allConferencesInDatabase = service.findAllConferences();
        for (Conference conference : allConferencesInDatabase) {
            if (conference.getConferenceTitle().equals(CONFERENCE_TITLE)) {
                conferenceId = conference.getId();
            }
        }

        // creating two users
        User userOneForTest = new User(USER_ONE_NICKNAME_FOR_VALIDATOR_TEST, "password");
        try {
            service.createUser(userOneForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        User newUserOneFromDBForTest = service.findByLogin(USER_ONE_NICKNAME_FOR_VALIDATOR_TEST);
        newUserOneFromDBId = newUserOneFromDBForTest.getId();

        User userTwoForTest = new User(USER_TWO_NICKNAME_FOR_VALIDATOR_TEST, "password");
        try {
            service.createUser(userTwoForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        User newUserFromDBForTest = service.findByLogin(USER_TWO_NICKNAME_FOR_VALIDATOR_TEST);
        newUserTwoFromDBId = newUserFromDBForTest.getId();

        // creating the first Section and fetching its id and name
        Section sectionForTest = new Section(1L, conferenceId, FIRST_SECTION_NAME, newUserOneFromDBId);
        try {
            service.createSection(sectionForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }

        // creating a second section in the created conference and fetching its id and name
        Section secondSectionForTest
                = new Section(1L, conferenceId, SECOND_SECTION_NAME, newUserOneFromDBId);
        try {
            service.createSection(secondSectionForTest);
            ;
        } catch (DuplicateException e) {
            e.printStackTrace();
        }

        List<Section> sectionsFromDB = service.findAllSectionsByConferenceID(conferenceId);
        for (Section section : sectionsFromDB
        ) {
            if (section.getSectionName().equals(FIRST_SECTION_NAME)) {
                sectionOneNameFromDB = section.getSectionName();
                sectionOneId = section.getId();
            }
            if (section.getSectionName().equals(SECOND_SECTION_NAME)) {
                sectionTwoNameFromDatabase = section.getSectionName();
                sectionTwoId = section.getId();
            }
        }
        Report testReportOne = new Report(1L, sectionOneId, conferenceId, REPORT_APPLICATION_ONE_TEXT,
                ReportType.APPLICATION, newUserOneFromDBId, 0L);
        try {
            service.createReport(testReportOne);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Report> reports = service.findAllReportsBySectionID(sectionOneId, conferenceId);
        for (Report report : reports
        ) {
            if (report.getReportText().equals(REPORT_APPLICATION_ONE_TEXT)) {
                reportApplicationOneId = report.getId();
            }
        }
    }

    @Test
    @Order(1)
    void isStringValidByValidString() {
        Assertions.assertTrue(validator.isStringValid(VALIDE_STRING_TO_VALIDATE));
    }

    @Test
    @Order(2)
    void isEmailValidByValidEmail() {
        Assertions.assertTrue(validator.isEmailValid(VALIDE_EMAIL_TO_VALIDATE));
    }

    @Test
    @Order(3)
    void isStringValidByInvalidString() {
        Assertions.assertFalse(validator.isStringValid(INVALIDE_STRING_TO_VALIDATE));
    }

    @Test
    @Order(4)
    void isEmailValidByInvalidEmail() {
        Assertions.assertFalse(validator.isEmailValid(INVALIDE_EMAIL_TO_VALIDATE));
    }


    @Test
    @Order(5)
    void isLengthValidByValidString() {
        Assertions.assertTrue(validator.
                isLengthValid(VALIDE_STRING_TO_VALIDATE_FOR_MAX_LENGTH, MAX_LENGTH_FOR_VALIDATOR));
    }

    @Test
    @Order(6)
    void isLengthValidByInvalidString() {
        Assertions.assertFalse(validator.
                isLengthValid(INVALIDE_STRING_TO_VALIDATE_FOR_MAX_LENGTH, MAX_LENGTH_FOR_VALIDATOR));
    }

    @Test
    @Order(7)
    void isConferenceExistInSystem() {
        Assertions.assertTrue(validator.isConferenceExistInSystem(conferenceId));
    }

    @Test
    @Order(8)
    void isUserWithIdExistInSystem() {
        Assertions.assertTrue(validator.isUserWithIdExistInSystem(newUserOneFromDBId));
    }

    @Test
    @Order(9)
    void isRoleExistInSystem() {
        Assertions.assertTrue(validator.isRoleExistInSystem(Role.ADMIN));
    }

    @Test
    @Order(10)
    void isSectionExistInSystem() {
        Assertions.assertTrue(validator.isSectionExistInSystem(sectionOneId));
    }

    @Test
    @Order(11)
    void isReportExistInSystem() {
        Assertions.assertTrue(validator.isReportExistInSystem(reportApplicationOneId));
    }

    @Test
    @Order(12)
    void isReportTypeExistInSystem() {
        Assertions.assertTrue(validator.isReportTypeExistInSystem("ANSWER"));
    }

    @Test
    @Order(13)
    void isUserWithNicknameExistInSystem() {
        Assertions.assertTrue(validator.isUserWithNicknameExistInSystem(USER_ONE_NICKNAME_FOR_VALIDATOR_TEST));
    }

    @Test
    @Order(14)
    void isSectionWithSuchNameExistInSystem() {
        Assertions.assertTrue(validator.isSectionWithSuchNameExistInSystem(FIRST_SECTION_NAME));
    }

    @Test
    @Order(15)
    void isConferenceWithSuchTitleExistInSystem() {
        Assertions.assertTrue(validator.isConferenceWithSuchTitleExistInSystem(CONFERENCE_TITLE));
    }

    @Test
    @Order(16)
    void isConferenceTitleAndIdFromTheSameConference() {
        Assertions.assertTrue(validator.isConferenceTitleAndIdFromTheSameConference(conferenceId, CONFERENCE_TITLE));
    }

    @Test
    @Order(17)
    void isSectionNameAndIdFromTheSameSection() {
        Assertions.assertTrue(validator.isSectionNameAndIdFromTheSameSection(sectionOneId, FIRST_SECTION_NAME));
    }

    @Test
    @Order(18)
    void isRoleWithSuchNameExistInSystem() {
        Assertions.assertTrue(validator.isRoleWithSuchNameExistInSystem("MANAGER"));
    }

    @Test
    @Order(19)
    void isUserIdAndUserRoleFromTheSameUser() {
        Assertions.assertFalse(validator.isUserIdAndUserRoleFromTheSameUser(newUserOneFromDBId.toString(),"MANAGER"));
    }

    @Test
    @Order(20)
    void isReportWithSuchTextExistInSystem() {
        Assertions.assertTrue(validator.isReportWithSuchTextExistInSystem(REPORT_APPLICATION_ONE_TEXT));
    }

    @AfterAll
    public void tearDown() {
        conferenceDAO.delete(conferenceId);
        service.deleteUser(newUserOneFromDBId);
        service.deleteUser(newUserTwoFromDBId);
        ConnectionPool.retrieve().destroy();
    }
}