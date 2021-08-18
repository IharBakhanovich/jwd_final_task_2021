package com.epam.jwd.conferences.service;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.UserDAO;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import com.epam.jwd.Conferences.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Tests for the UserService/ConferenceService
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConferenceServiceTest {
    String conferenceTitle = "testConference";
    Long managerConference = 1L;

    Conference conferenceForTest = new Conference(1L, conferenceTitle, managerConference);
    ConferenceDAO conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    Long idConferenceForTest;
    UserService service = UserService.retrieve();
    String updatedConferenceTitle = "Forza VR46";
    List<Conference> allConferencesInDatabase;

    @BeforeAll
    public void setUp() {
        try {
            ConnectionPool.retrieve().init();
        } catch (CouldNotInitializeConnectionPoolException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void createConference() {
        try {
            service.createConference(conferenceForTest);
        } catch (DuplicateException e) {
            e.printStackTrace();
        }
        allConferencesInDatabase = service.findAllConferences();
        String conferenceTitleFromDB = null;
        for (Conference conference : allConferencesInDatabase) {
            if (conference.getConferenceTitle().equals(conferenceTitle)) {
                idConferenceForTest = conference.getId();
                conferenceTitleFromDB = conference.getConferenceTitle();
            }
        }
        Assertions.assertEquals(conferenceTitleFromDB, conferenceTitle);
    }

    @Test
    @Order(2)
    void updateConference() {
        Conference conferencefromDatabase = null;
        for (Conference conference : allConferencesInDatabase) {
            if (conference.getId().equals(idConferenceForTest)) {
                conferencefromDatabase = conference;
                break;
            }
        }
        Conference updatedConference = null;
        if (conferencefromDatabase!=null) {
            updatedConference
                    = new Conference(idConferenceForTest, updatedConferenceTitle, conferencefromDatabase.getManagerConf());
        }

        service.updateConference(updatedConference);
        List<Conference> conferencesAfterUpdate = service.findAllConferences();

        String updatedConferenceTitleFromDB = null;
        for (Conference conference : conferencesAfterUpdate) {
            if (conference.getId().equals(idConferenceForTest)) {
                updatedConferenceTitleFromDB = conference.getConferenceTitle();
                break;
            }
        }
        Assertions.assertEquals(updatedConferenceTitleFromDB, updatedConferenceTitle);
    }

    @AfterAll
    public void tearDown() {
        conferenceDAO.delete(idConferenceForTest);
        allConferencesInDatabase.clear();
        ConnectionPool.retrieve().destroy();
    }
}