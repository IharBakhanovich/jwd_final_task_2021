package com.epam.jwd.Conferences.dto;

import java.util.Objects;

/**
 * Stores the Section.
 */
public class Section implements DatabaseEntity<Long> {
    private final Long id;
    private final Long conferenceId;
    private final String sectionName;
    private final Long managerSect;

    /**
     * Constructs a new {@link Section}.
     *
     * @param id           is the value of the id of the new {@link Section}
     * @param conferenceId is the value of the conferenceId of the new {@link Section}
     * @param sectionName  is the value of the reportText of the new {@link Section}
     * @param managerSect  is the value of the the reportType of the new {@link Section}
     */
    public Section(Long id, Long conferenceId, String sectionName, Long managerSect) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.sectionName = sectionName;
        this.managerSect = managerSect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * A Getter for the conferenceId.
     *
     * @return The conferenceId.
     */
    public Long getConferenceId() {
        return conferenceId;
    }

    /**
     * A Getter for the sectionName.
     *
     * @return The sectionName.
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * A Getter for the sectionName.
     *
     * @return The sectionName.
     */
    public Long getManagerSect() {
        return managerSect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id.equals(section.id)
                && conferenceId.equals(section.conferenceId)
                && sectionName.equals(section.sectionName)
                && managerSect.equals(section.managerSect);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, conferenceId, sectionName, managerSect);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", conferenceId=" + conferenceId +
                ", sectionName='" + sectionName + '\'' +
                ", managerSect=" + managerSect +
                '}';
    }
}