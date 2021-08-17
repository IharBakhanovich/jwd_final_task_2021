package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Conference;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;

/**
 * The interface to realize section service of the system.
 */
public interface SectionService {

    static SectionService retrieve() {
        return AppSectionService.getInstance();
    }

    /**
     * Returns all the {@link Section}s of the {@link Conference} with the id equals {@param id}.
     *
     * @param id is the id of the {@link Conference} which {@link Section}s are to return.
     * @return all the {@link Section}s of the {@link Conference} with the id equals {@param id}.
     */
    List<Section> findAllSectionsByConferenceID(Long id);

    /**
     * Returns all {@link Section}s in the system.
     *
     * @return {@link List<Section>} that contains all sections in the system.
     */
    List<Section> findAllSections();

    /**
     * Creates a new {@link Section} in the system.
     *
     * @param sectionToCreate is the {@link Section} to create.
     * @throws DuplicateException if there is the {@link Section}
     *                            with the sectionName of the {@param sectionToCreate}
     *                            and there is a violation of unique constrains in the database.
     */
    void createSection(Section sectionToCreate) throws DuplicateException;

    /**
     * Updates the {@link Section}.
     *
     * @param sectionToUpdate is the {@link Section} to update.
     */
    void updateSection(Section sectionToUpdate);
}
