package com.epam.jwd.conferences.dao;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.ReportDAO;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.ReportType;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;

/**
 * Test for the DBReportDAO
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBReportDAOTest {
    private final ConferenceDAO conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    private final SectionDAO sectionDAO = DAOFactory.getInstance().getSectionDAO();
    private final ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    private List<Conference> conferences;
    private List<Section> sections;
    private List<Report> reports;
    Conference conference = new Conference(1L, "TestConference", 1L);
    Section section = null;
    Report report = null;
    private Long sectionId;
    private Long conferenceId;
    private Long reportId;
    String newReportText;
    Long conferenceManager;

    @BeforeAll
    public void setUp() {
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
        try {
            conferenceDAO.save(conference);
            conferences = conferenceDAO.findAll();
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        for (Conference conferenceInLoop : conferences
        ) {
            if (conferenceInLoop.getConferenceTitle().equals(conference.getConferenceTitle())) {
                conferenceId = conferenceInLoop.getId();
                conferenceManager = conferenceInLoop.getManagerConf();
            }
        }
        section = new Section(1L, conferenceId, "TestConference", conferenceManager);
        try {
            sectionDAO.save(section);
            sections = sectionDAO.findAllSectionsByConferenceID(conferenceId);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        for (Section sectionInLoop : sections
        ) {
            if (sectionInLoop.getSectionName().equals(section.getSectionName())) {
                sectionId = sectionInLoop.getId();
            }
        }
        report = new Report(1L, sectionId, conferenceId, "TestReport",
                ReportType.APPLICATION, conferenceManager, 0L);
        try {
            reportDAO.save(report);
            reports = reportDAO.findAllReportsBySectionID(sectionId, conferenceId);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        for (Report reportInLoop : reports
        ) {
            if (reportInLoop.getReportText().equals(report.getReportText())) {
                reportId = reportInLoop.getId();
            }
        }
    }

    @Test
    @Order(1)
    void InsertReport() {
        Long reportIdFromDatabase = null;

        for (Report reportInLoop : reports
        ) {
            if (reportInLoop.getReportText().equals(report.getReportText())) {
                reportIdFromDatabase = reportInLoop.getId();
            }
        }
        Assertions.assertEquals(reportIdFromDatabase, reportId);
    }

    @Test
    @Order(2)
    void findReport() {
        Optional<Report> reportFromDatabase = reportDAO.findById(reportId);
        Assertions.assertEquals(reportFromDatabase.get().getReportText(), report.getReportText());
    }

    @Test
    @Order(3)
    void UpdateReport() {
        String newReportText = "TestsPassedReport";
        Report updatedReport
                = new Report(reportId, report.getSectionId(), report.getConferenceId(),
                newReportText, report.getReportType(), report.getApplicant(), report.getQuestionReportId());
        reportDAO.update(updatedReport);

        Optional<Report> reportFromDatabase = reportDAO.findById(reportId);
        this.newReportText = reportFromDatabase.get().getReportText();
        Assertions.assertEquals(reportFromDatabase.get().getReportText(),
                updatedReport.getReportText());
    }

    @Test
    @Order(4)
    void DeleteSection() {
        Optional<Report> reportFromDatabase = reportDAO.findById(reportId);

        if (reportFromDatabase.get().getReportText().equals(newReportText)) {
            reportDAO.delete(reportId);
            Optional<Report> foundedReport = reportDAO.findById(reportId);
            Assertions.assertFalse(foundedReport.isPresent());
        } else {
            Assertions.fail();
        }
    }


    @AfterAll
    public void tearDown() {
        conferenceDAO.delete(conferenceId);
        sectionDAO.delete(sectionId);
        conferences.clear();
        sections.clear();
        ConnectionPool.retrieve().destroy();
    }
}