package com.epam.jwd.Conferences.dto;

import java.util.Objects;

/**
 * Stores report data.
 */
public class Report implements DatabaseEntity<Long> {

    private final Long id;
    private final Long sectionId;
    private final Long conferenceId;
    private final String reportText;
    private final ReportType reportType;
    private final Long applicant;

    public Report(Long id, Long sectionId,
                  Long conferenceId, String reportText,
                  ReportType reportType, Long applicant) {
        this.id = id;
        this.sectionId = sectionId;
        this.conferenceId = conferenceId;
        this.reportText = reportText;
        this.reportType = reportType;
        this.applicant = applicant;
    }

    /**
     * A Getter for the report id.
     *
     * @return The report id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * A Getter for the sectionId.
     *
     * @return The sectionId
     */
    public Long getSectionId() {
        return sectionId;
    }

    /**
     * A Getter for the conferenceId.
     *
     * @return The conference id
     */
    public Long getConferenceId() {
        return conferenceId;
    }

    /**
     * A Getter for the reportText.
     *
     * @return The reportText
     */
    public String getReportText() {
        return reportText;
    }

    /**
     * A Getter for the reportType.
     *
     * @return The reportType
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * A Getter for the applicant.
     *
     * @return The applicant
     */
    public Long getApplicant() {
        return applicant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id.equals(report.id)
                && sectionId.equals(report.sectionId)
                && conferenceId.equals(report.conferenceId)
                && reportText.equals(report.reportText)
                && reportType == report.reportType
                && applicant.equals(report.applicant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sectionId, conferenceId, reportText, reportType, applicant);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", sectionId=" + sectionId +
                ", conferenceId=" + conferenceId +
                ", reportText='" + reportText + '\'' +
                ", reportType=" + reportType +
                ", applicant=" + applicant +
                '}';
    }
}
