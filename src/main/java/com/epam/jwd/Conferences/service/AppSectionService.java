package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.ReportDAO;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dto.Section;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;

/**
 * This is a service (model) of the application. The singleton.
 */
public class AppSectionService implements SectionService {

    private final SectionDAO sectionDAO;

    private AppSectionService() {
        this.sectionDAO = DAOFactory.getInstance().getSectionDAO();
    }

    private static class AppSectionServiceHolder {
        private final static AppSectionService instance
                = new AppSectionService();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppSectionService getInstance() {
        return AppSectionService.AppSectionServiceHolder.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Section> findAllSectionsByConferenceID(Long id) {
        return sectionDAO.findAllSectionsByConferenceID(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Section> findAllSections() {
        return sectionDAO.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSection(Section sectionToCreate) throws DuplicateException {
        sectionDAO.save(sectionToCreate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSection(Section sectionToUpdate) {
        sectionDAO.update(sectionToUpdate);
    }
}
