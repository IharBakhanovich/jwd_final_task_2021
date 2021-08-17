package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Section;

import java.util.List;
import java.util.Optional;

public interface SectionDAO extends DAO<Section, Long> {

    /**
     * Finds {@link Optional<Section>} by the value of the {@link Section}s sectionName parameter.
     *
     * @param title is the value of the of the {@link Section}s sectionName parameter to search.
     * @return {@link Optional<Section>} which parameter sectionName equals to the calue of the {@param title}.
     */
    Optional<Section> findSectionByTitle(String title);

    /**
     * Finds all the sections of the {@link com.epam.jwd.Conferences.dto.Conference} with the conferenceId
     * equals to the {@param id}.
     *
     * @param id is the value of the {@link com.epam.jwd.Conferences.dto.Conference}s id parameter,
     *           which sections to find
     * @return {@link List<Section>} with all the {@link Section}s of the conference with the id
     * equals to the {@param id}.
     */
    List<Section> findAllSectionsByConferenceID(Long id);

    /**
     * Returns the implementation of the SectionDAO.
     *
     * @return Object that is the implementation of the ConferenceDAO.
     */
    static SectionDAO retrieve() {
        return DBSectionDAO.getInstance();
    }
}