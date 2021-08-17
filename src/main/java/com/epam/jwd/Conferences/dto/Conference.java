package com.epam.jwd.Conferences.dto;

import java.util.Objects;

/**
 * Stores the Conference.
 */
public class Conference implements DatabaseEntity<Long> {
    private final Long id;
    private final String conferenceTitle;
    private final Long managerConf;

    /**
     * Constructs a new {@link Conference}.
     *
     * @param id              is the value of the id of the new {@link Conference}
     * @param conferenceTitle is the value of the conferenceTitle of the new {@link Conference}
     * @param managerConf     is the value of the managerConf of the new {@link Conference}
     */
    public Conference(Long id, String conferenceTitle, Long managerConf) {
        this.id = id;
        this.conferenceTitle = conferenceTitle;
        this.managerConf = managerConf;
    }

    /**
     * A Getter for the Conference id.
     *
     * @return The Conference id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * A Getter for the Conference title.
     *
     * @return The Conference title.
     */
    public String getConferenceTitle() {
        return conferenceTitle;
    }

    /**
     * A Getter for the Conference manager.
     *
     * @return The Conference manager.
     */
    public Long getManagerConf() {
        return managerConf;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", conferenceTitle='" + conferenceTitle + '\'' +
                ", managerConf=" + managerConf +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, conferenceTitle, managerConf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return id.equals(that.id)
                && conferenceTitle.equals(that.conferenceTitle)
                && managerConf.equals(that.managerConf);
    }
}
