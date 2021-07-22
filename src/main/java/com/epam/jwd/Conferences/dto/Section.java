package com.epam.jwd.Conferences.dto;

import java.util.Objects;

public class Section implements DatabaseEntity<Long> {
    private final Long id;
    private final Long conferenceId;
    private final String sectionName;
    private final Long managerSect;

    public Section(Long id, Long conferenceId, String sectionName, Long managerSect) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.sectionName = sectionName;
        this.managerSect = managerSect;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Long getManagerSect() {
        return managerSect;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, conferenceId, sectionName, managerSect);
    }

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
