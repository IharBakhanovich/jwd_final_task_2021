package com.epam.jwd.Conferences.dto;

import java.util.Objects;

/**
 * Stores report data.
 */
public class Report implements DatabaseEntity<Long>, Comparable<Report> {

    private final Long id;
    private final Long sectionId;
    private final Long conferenceId;
    private final String reportText;
    private final ReportType reportType;
    private final Long applicant;
    // the reference to the report (question), to which this report belongs as an answer
    private final Long questionReportId;

    /**
     * Constructs a new {@link Report}.
     *
     * @param id is the value of the id of the new {@link Report}
     * @param sectionId is the value of the sectionId of the new {@link Report}
     * @param conferenceId is the value of the conferenceId of the new {@link Report}
     * @param reportText is the value of the reportText of the new {@link Report}
     * @param reportType is the value of the the reportType of the new {@link Report}
     * @param applicant is the value of the applicant of the new {@link Report}
     * @param questionReportId is the value of the questionReportId of the new {@link Report}
     */
    public Report(Long id, Long sectionId,
                  Long conferenceId, String reportText,
                  ReportType reportType, Long applicant, Long questionReportId) {
        this.id = id;
        this.sectionId = sectionId;
        this.conferenceId = conferenceId;
        this.reportText = reportText;
        this.reportType = reportType;
        this.applicant = applicant;
        this.questionReportId = questionReportId;
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

    /**
     * A Getter for the questionReportId.
     *
     * @return The questionReportId
     */
    public Long getQuestionReportId() {
        return questionReportId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id)
                && Objects.equals(sectionId, report.sectionId)
                && Objects.equals(conferenceId, report.conferenceId)
                && Objects.equals(reportText, report.reportText)
                && reportType == report.reportType
                && Objects.equals(applicant, report.applicant)
                && Objects.equals(questionReportId, report.questionReportId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, sectionId, conferenceId, reportText, reportType, applicant, questionReportId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", sectionId=" + sectionId +
                ", conferenceId=" + conferenceId +
                ", reportText='" + reportText + '\'' +
                ", reportType=" + reportType +
                ", applicant=" + applicant +
                ", questionReportId=" + questionReportId +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Report o) {
        final Long id = getId();
        final Long otherId = o.getId();
        return id.compareTo(otherId);
    }
}