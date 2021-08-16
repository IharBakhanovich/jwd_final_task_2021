package com.epam.jwd.conferences.dao;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.pool.ConnectionPool;

import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;
import java.util.Optional;

/**
 * Test for the DBConferenceDAO
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class DBConferenceDAOTest {

    private final ConferenceDAO conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    private List<Conference> conferences;
    Conference conference = new Conference(1L, "TestConference", 1L);
    private Long conferenceId;
    String newConferenceTitle;

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
            }
        }
    }

    @Test
    @Order(1)
    void InsertConference() {
        Long conferenceIdFromDatabase = null;

        for (Conference conferenceInLoop : conferences
        ) {
            if (conferenceInLoop.getConferenceTitle().equals(conference.getConferenceTitle())) {
                conferenceIdFromDatabase = conferenceInLoop.getId();
            }
        }
        Assertions.assertEquals(conferenceIdFromDatabase, conferenceId);
    }

    @Test
    @Order(2)
    void InsertConferenceDuplicate() {
        Assertions.assertThrows(DuplicateException.class, () -> conferenceDAO.save(conference));
    }

    @Test
    @Order(3)
    void findConference() {
        Optional<Conference> conferenceFromDatabase = conferenceDAO.findById(conferenceId);
        if (conferenceFromDatabase.isPresent()) {
            Assertions.assertEquals(conferenceFromDatabase.get().getConferenceTitle(), conference.getConferenceTitle());
        } else {
            Assertions.fail();
        }

    }

    @Test
    @Order(4)
    void UpdateConference() {
        String newConferenceTitle = "TestsPassedConference";
        Conference updatedConference = new Conference(conferenceId, newConferenceTitle, conference.getManagerConf());
        conferenceDAO.update(updatedConference);

        Optional<Conference> conferenceFromDatabase = conferenceDAO.findById(conferenceId);
        if (conferenceFromDatabase.isPresent()) {
            this.newConferenceTitle = conferenceFromDatabase.get().getConferenceTitle();
            Assertions.assertEquals(conferenceFromDatabase.get().getConferenceTitle(), updatedConference.getConferenceTitle());
        } else {
            Assertions.fail();
        }
    }

    @Test
    @Order(5)
    void DeleteConference() {
        Optional<Conference> conferenceFromDatabase = conferenceDAO.findById(conferenceId);
        if (conferenceFromDatabase.isPresent()) {
            if (conferenceFromDatabase.get().getConferenceTitle().equals(newConferenceTitle)) {
                conferenceDAO.delete(conferenceId);
                Optional<Conference> foundedConference = conferenceDAO.findById(conferenceId);
                Assertions.assertFalse(foundedConference.isPresent());
            } else {
                Assertions.fail();
            }
        } else {
            Assertions.fail();
        }
    }

    @AfterAll
    public void tearDown() {
        conferences.clear();
        ConnectionPool.retrieve().destroy();
    }
}
