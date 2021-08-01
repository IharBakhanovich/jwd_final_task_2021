package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;

public interface SectionService {

    static SectionService retrieve() {
        return AppSectionService.getInstance();
    }

    List<Section> findAllSectionsByConferenceID(Long id);

    List<Section> findAllSections();

    void createSection(Section sectionToCreate) throws DuplicateException;

    void updateSection(Section sectionToUpdate);
}
