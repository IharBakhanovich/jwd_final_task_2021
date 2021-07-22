package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Section;

import java.util.List;
import java.util.Optional;

public interface SectionDAO extends DAO<Section,Long> {

    Optional<Section> findSectionByTitle(String title);

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
