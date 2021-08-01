package com.epam.jwd.Conferences.service;

import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.util.List;
import java.util.Optional;

public interface ReportService {

    static ReportService retrieve() {
        return AppReportService.getInstance();
    }

    List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId);

    Optional<Report> findReportByID(Long id);

    void updateReport(Report reportToUpdate);

    void createReport(Report reportToCreate) throws DuplicateException;

}
