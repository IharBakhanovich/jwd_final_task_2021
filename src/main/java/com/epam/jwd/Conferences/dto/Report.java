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

    @Override
    public int hashCode() {
        return Objects.hash(id, sectionId, conferenceId, reportText, reportType, applicant, questionReportId);
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
                ", questionReportId=" + questionReportId +
                '}';
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Report o) {
        final Long id = getId();
        final Long otherId = o.getId();
        return id.compareTo(otherId);
    }
}
