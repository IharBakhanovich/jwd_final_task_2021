package com.epam.jwd.conferences.dao;

import com.epam.jwd.Conferences.dao.ConferenceDAO;
import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dto.Conference;
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
 * Test for the DBConferenceDAO
 */
@RunWith(JUnitPlatform.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBSectionDaoTest {
    private final ConferenceDAO conferenceDAO = DAOFactory.getInstance().getConferenceDAO();
    private final SectionDAO sectionDAO = DAOFactory.getInstance().getSectionDAO();
    private List<Conference> conferences;
    private List<Section> sections;
    Conference conference = new Conference(1L, "TestConference", 1L);
    Section section = null; //new Section(1L, 10L, "TestSection", 1L);
    private Long sectionId;
    private Long conferenceId;
    String newSectionName;
    String newConferenceTitle;
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
    }

    @Test
    @Order(1)
    void InsertSection() {
        Long sectionIdFromDatabase = null;

        for (Section sectionInLoop : sections
        ) {
            if (sectionInLoop.getSectionName().equals(section.getSectionName())) {
                sectionIdFromDatabase = sectionInLoop.getId();
            }
        }
        Assertions.assertEquals(sectionIdFromDatabase, sectionId);
    }

    @Test
    @Order(2)
    void findSection() {
        Optional<Section> sectionFromDatabase = sectionDAO.findById(sectionId);
        Assertions.assertEquals(sectionFromDatabase.get().getSectionName(), section.getSectionName());
    }

    @Test
    @Order(3)
    void UpdateSection() {
        String newSectionName = "TestsPassedSection";
        Section updatedSection
                = new Section(sectionId, section.getConferenceId(), newSectionName, section.getManagerSect());
        sectionDAO.update(updatedSection);

        Optional<Section> sectionFromDatabase = sectionDAO.findById(sectionId);
        this.newSectionName = sectionFromDatabase.get().getSectionName();
        Assertions.assertEquals(sectionFromDatabase.get().getSectionName(), updatedSection.getSectionName());
    }

    @Test
    @Order(4)
    void DeleteSection() {
        Optional<Section> sectionFromDatabase = sectionDAO.findById(sectionId);

        if (sectionFromDatabase.get().getSectionName().equals(newSectionName)) {
            sectionDAO.delete(sectionId);
            Optional<Section> foundedSection = sectionDAO.findById(sectionId);
            Assertions.assertFalse(foundedSection.isPresent());
        } else {
            Assertions.fail();
        }
    }


    @AfterAll
    public void tearDown() {
        conferenceDAO.delete(conferenceId);
        conferences.clear();
        sections.clear();
        ConnectionPool.retrieve().destroy();
    }

}
