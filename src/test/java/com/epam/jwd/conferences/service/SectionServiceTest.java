package com.epam.jwd.conferences.service;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Tests for the SectionService
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SectionServiceTest {
    String conferenceTitle = "testConference";
    String sectionName = "testSection";
    Long managerConference = 1L;
    Long managerSection = 1L;

    Conference conferenceForTest = new Conference(1L, conferenceTitle, managerConference);
    ConferenceDAO conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    SectionDAO sectionDAO = DAOFactory.getInstance().getSectionDAO();
    Long idConferenceForTest;
    Long idSectionForTest;
    UserService service = UserService.retrieve();
    String updatedSectionTitle = "RossiForever";
    List<Conference> allConferencesInDatabase;
    Long newUserFromDBId;

    @BeforeAll
    public void setUp() {
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
        try {
            service.createConference(conferenceForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        allConferencesInDatabase = service.findAllConferences();
        for (Conference conference : allConferencesInDatabase) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                idConferenceForTest = conference.getId();
            }
        }
    }

    @Test
    @Order(1)
    void createSection() {
        Section sectionForTest = new Section(1L, idConferenceForTest, sectionName, managerSection);
        try {
            service.createSection(sectionForTest);;
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Section> sectionsFromDB = service.findAllSectionsByConferenceID(idConferenceForTest);
        String sectionNameFromDB = null;
        for (Section section: sectionsFromDB
             ) {
            if (section.getSectionName().equals(sectionName)) {
                sectionNameFromDB = section.getSectionName();
                idSectionForTest = section.getId();
            }
        }
        Assertions.assertEquals(sectionNameFromDB, sectionName);
    }

    @Test
    @Order(2)
    void findAllSectionsByConferenceId() {
        Section secondSectionForTest
                = new Section(1L, idConferenceForTest, "RossiRossiRossi", managerSection);
        try {
            service.createSection(secondSectionForTest);;
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        List<Section> sectionsFromDB = service.findAllSectionsByConferenceID(idConferenceForTest);
        Assertions.assertEquals(2, sectionsFromDB.size());
    }

    @Test
    @Order(3)
    void findAllSectionsWhereUserIsManager() {
        List<Section> sectionsFromCreatedConference = service.findAllSectionsByConferenceID(idConferenceForTest);
        String newUserForTestNickname = "userForSectionTest";
        User userForTest = new User (newUserForTestNickname, "password");
        try {
            service.createUser(userForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        User newUserFromDBForTest = service.findByLogin(newUserForTestNickname);
        newUserFromDBId = newUserFromDBForTest.getId();
        Section firstSectionToUpdate
                = new Section (sectionsFromCreatedConference.get(0).getId(),
                sectionsFromCreatedConference.get(0).getConferenceId(),
                sectionsFromCreatedConference.get(0).getSectionName(),
                newUserFromDBId);
        service.updateSection(firstSectionToUpdate);
        Section secondSectionToUpdate
                = new Section (sectionsFromCreatedConference.get(1).getId(),
                sectionsFromCreatedConference.get(1).getConferenceId(),
                sectionsFromCreatedConference.get(1).getSectionName(),
                newUserFromDBId);
        service.updateSection(secondSectionToUpdate);

        List<Section> newUserAsManagerSections = service.findAllSectionsWhereUserIsManager(newUserFromDBId);

        Assertions.assertEquals(2, newUserAsManagerSections.size());
    }

    @Test
    @Order(4)
    void updateSection() {
        List<Section> sectionsFromCreatedConference = service.findAllSectionsByConferenceID(idConferenceForTest);

        Section secondToUpdate
                = new Section(idSectionForTest, idConferenceForTest, updatedSectionTitle, managerConference);
        service.updateSection(secondToUpdate);
        List<Section> conferenceSectionsAfterUpdate = service.findAllSectionsByConferenceID(idConferenceForTest);
        Assertions.assertNotEquals(conferenceSectionsAfterUpdate.get(0).getManagerSect(),
                conferenceSectionsAfterUpdate.get(1).getManagerSect());
    }

    @AfterAll
    public void tearDown() {
        conferenceDAO.delete(idConferenceForTest);
        service.deleteUser(newUserFromDBId);
        allConferencesInDatabase.clear();
        ConnectionPool.retrieve().destroy();
    }
}
