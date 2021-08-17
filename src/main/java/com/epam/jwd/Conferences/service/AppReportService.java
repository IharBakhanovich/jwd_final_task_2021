package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dao.DAOFactory;
import com.epam.jwd.Conferences.dao.ReportDAO;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This is a service (model) of the application. The singleton.
 */
public class AppReportService implements ReportService {

    private final ReportDAO reportDAO;

    private AppReportService() {
        this.reportDAO = DAOFactory.getInstance().getReportDAO();
    }

    private static class AppReportServiceHolder {
        private final static AppReportService instance
                = new AppReportService();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static AppReportService getInstance() {
        return AppReportService.AppReportServiceHolder.instance;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId) {
        return reportDAO.findAllReportsBySectionID(sectionId, conferenceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Report> findReportByID(Long id) {
        return reportDAO.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReport(Report reportToUpdate) {
        reportDAO.update(reportToUpdate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReport(Report reportToCreate) throws DuplicateException {
        reportDAO.save(reportToCreate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Report> findAllQuestions(Long managerId) {
        return reportDAO.findAllQuestionsByManagerId(managerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Report> findAllReportsByQuestionId(Long questionReportId) {
        Optional<Report> question = reportDAO.findById(questionReportId);
        List<Report> answers = reportDAO.findAllReportsByQuestionReportId(questionReportId);

        if (question.isPresent()) {
            final Report questionToAdd = new Report(question.get().getId(), question.get().getSectionId(),
                    question.get().getConferenceId(), question.get().getReportText(), question.get().getReportType(),
                    question.get().getApplicant(), question.get().getQuestionReportId());
            answers.add(questionToAdd);
            Collections.sort(answers);
        }
        return answers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Report> findAllApplications(Long managerId) {
        return reportDAO.findAllApplicationsByManagerId(managerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Report> findApplicantApplications(Long applicantId) {
        return reportDAO.findAllApplicationsByApplicantId(applicantId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Report> findApplicantQuestions(Long managerId) {
        return reportDAO.findAllQuestionsByApplicantId(managerId);
    }
}