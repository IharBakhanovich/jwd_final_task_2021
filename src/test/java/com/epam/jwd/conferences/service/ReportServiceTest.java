package com.epam.jwd.conferences.service;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dto.*;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;

/**
 * Tests for the ReportService
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportServiceTest {
    public static final String SECOND_SECTION_NAME = "RossiRossiRossi";
    public static final String CONFERENCE_TITLE = "testConference";
    public static final String FIRST_SECTION_NAME = "testSection";
    public static final Long MANAGER_CONFERENCE_ID = 1L;
    public static final Long MANAGER_SECTION_ID = 1L;
    public static final String REPORT_APPLICATION_ONE_TEXT = "testApplicationReport";
    public static final String REPORT_QUESTION_TEXT = "testQuestionReport";
    public static final String UPDATED_REPORT_QUESTION_TEXT = "RossiForever";
    public static final String USER_ONE_NICKNAME_FOR_REPORT_SERVICE_TEST = "userOneForReportServiceTest";
    public static final String USER_TWO_NICKNAME_FOR_REPORT_SERVICE_TEST = "userTwoForReportServiceTest";

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
        User userOneForTest = new User(USER_ONE_NICKNAME_FOR_REPORT_SERVICE_TEST, "password");
        try {
            service.createUser(userOneForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        User newUserOneFromDBForTest = service.findByLogin(USER_ONE_NICKNAME_FOR_REPORT_SERVICE_TEST);
        newUserOneFromDBId = newUserOneFromDBForTest.getId();

        User userTwoForTest = new User(USER_TWO_NICKNAME_FOR_REPORT_SERVICE_TEST, "password");
        try {
            service.createUser(userTwoForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        User newUserFromDBForTest = service.findByLogin(USER_TWO_NICKNAME_FOR_REPORT_SERVICE_TEST);
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
        allreportsInTheSystemBeforeTests = service.findAllReports();
    }

    @Test
    @Order(1)
    void createReport() {
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
        Assertions.assertEquals(reports.get(0).getReportText(), REPORT_APPLICATION_ONE_TEXT);
    }

    @Test
    @Order(2)
    void findReportById() {
        Optional<Report> applicationReportOneFoundById = service.findReportByID(reportApplicationOneId);
        applicationReportOneFoundById
                .ifPresent(report -> Assertions.assertEquals(report.getReportText(), REPORT_APPLICATION_ONE_TEXT));
    }

    @Test
    @Order(3)
    void findAllReportsBySectionId() {
        // creating the question in the same section of the same conference
        Report testReportTwo = new Report(1L, sectionOneId, conferenceId, REPORT_QUESTION_TEXT,
                ReportType.QUESTION, newUserOneFromDBId, 0L);
        try {
            service.createReport(testReportTwo);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Report> reports = service.findAllReportsBySectionID(sectionOneId, conferenceId);
        Assertions.assertEquals(2, reports.size());
    }

    @Test
    @Order(4)
    void findAllApplicantQuestions() {
        // creating the question in the another section of the same conference
        Report testReportThree = new Report(1L, sectionTwoId, conferenceId, REPORT_QUESTION_TEXT,
                ReportType.QUESTION, newUserOneFromDBId, 0L);
        try {
            service.createReport(testReportThree);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Report> reports = service.findApplicantQuestions(newUserOneFromDBId);
        Assertions.assertEquals(2, reports.size());
    }

    @Test
    @Order(5)
    void findAllApplicantApplication() {
        // creating the application in the another section of the same conference
        Report testReportFour = new Report(1L, sectionTwoId, conferenceId, REPORT_APPLICATION_ONE_TEXT,
                ReportType.APPLICATION, newUserOneFromDBId, 0L);
        try {
            service.createReport(testReportFour);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Report> reports = service.findApplicantApplications(newUserOneFromDBId);
        Assertions.assertEquals(2, reports.size());
    }

    @Test
    @Order(6)
    void findAllApplication() {
        // creating the application in the another section of the same conference but by another user as applicant
        Report testReportFive = new Report(1L, sectionTwoId, conferenceId, REPORT_APPLICATION_ONE_TEXT,
                ReportType.APPLICATION, newUserTwoFromDBId, 0L);
        try {
            service.createReport(testReportFive);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Report> reports = service.findAllApplications(newUserOneFromDBId);
        Assertions.assertEquals(3, reports.size());
    }

    @Test
    @Order(7)
    void findAllQuestions() {
        // creating the question in the another section of the same conference but by another user as applicant
        Report testReportFive = new Report(1L, sectionTwoId, conferenceId, REPORT_QUESTION_TEXT,
                ReportType.QUESTION, newUserTwoFromDBId, 0L);
        try {
            service.createReport(testReportFive);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Report> reports = service.findAllQuestions(newUserOneFromDBId);
        Assertions.assertEquals(3, reports.size());
    }

    @Test
    @Order(8)
    void findAllReportsByUserId() {
        List<Report> reports = service.findAllReportsByUserId(newUserOneFromDBId);
        Assertions.assertEquals(4, reports.size());
    }

    @Test
    @Order(9)
    void updateReport() {
        /*
         creating the report with id of the reportOne, but changing its reportType to ReportType.QUESTION
         and its reportText to the REPORT_QUESTION_TEXT
         */
        // question in the another section of the same conference but by another user as applicant
        Report testReportToUpdate = new Report(reportApplicationOneId, sectionOneId, conferenceId,
                UPDATED_REPORT_QUESTION_TEXT, ReportType.QUESTION, newUserTwoFromDBId, 0L);
        service.updateReport(testReportToUpdate);
        Optional<Report> reportOneFromDB = service.findReportByID(reportApplicationOneId);
        reportOneFromDB
                .ifPresent(report -> Assertions.assertTrue(
                        report.getReportText().equals(UPDATED_REPORT_QUESTION_TEXT)
                                && report.getReportType().equals(ReportType.QUESTION)
                ));
    }

    @Test
    @Order(10)
    void findAllReportsByQuestionId() {
        final List<Report> questionsToReport = service.findAllReportsByQuestionId(reportApplicationOneId);
        // it should be only this report themself
        Assertions.assertEquals(1, questionsToReport.size());
    }

    @Test
    @Order(11)
    void deleteReport() {
        service.deleteReport(reportApplicationOneId);
        Optional<Report> deletedReportFromDB = service.findReportByID(reportApplicationOneId);

        Assertions.assertFalse(deletedReportFromDB.isPresent());
    }

    @Test
    @Order(12)
    void findAllReports() {

        List<Report> allReportsInTheSystem = service.findAllReports();

        Assertions.assertEquals(allReportsInTheSystem.size(), 5 + allreportsInTheSystemBeforeTests.size());
    }

    @AfterAll
    public void tearDown() {
        conferenceDAO.delete(conferenceId);
        service.deleteUser(newUserOneFromDBId);
        service.deleteUser(newUserTwoFromDBId);
        allreportsInTheSystemBeforeTests.clear();
        ConnectionPool.retrieve().destroy();
    }
}
