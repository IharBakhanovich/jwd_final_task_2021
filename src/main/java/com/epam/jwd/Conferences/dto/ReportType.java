package com.epam.jwd.Conferences.dto;

import com.epam.jwd.Conferences.exception.UnknownEntityException;

import java.util.Arrays;
import java.util.List;

/**
 * Stores the ReportType.
 */
public enum ReportType implements DatabaseEntity<Long> {
    QUESTION(1L),
    APPLICATION(2L),
    ANSWER(3L),
    APPROVED(4L),
    REJECTED(5L),
    CANCELLED(6L);


    private final Long id;

    /**
     * Constructs a new {@link ReportType}.
     *
     * @param id is the value of the id of the new {@link ReportType}
     */
    ReportType(Long id) {
        this.id = id;
    }

    /**
     * creates the {@link List<ReportType>} with all the values of the {@link ReportType}.
     */
    public static final List<ReportType> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    /**
     * Returns the List of all the ReportType values.
     *
     * @return {@link List<ReportType>}.
     */
    public static List<ReportType> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Returns the value of the 'name' parameter of this ReportType.
     *
     * @return the name of the {@link ReportType}
     */
    public String getName() {
        return this.name();
    }

    /**
     * Returns ReportType by its id.
     *
     * @param id is the id of the ReportType.
     * @throws UnknownEntityException if such id does not exist.
     */
    public static ReportType resolveReportTypeById(Long id) {
        for (ReportType reportType : ReportType.values()
        ) {
            if (reportType.getId().equals(id)) {
                return reportType;
            }
        }
        throw new UnknownEntityException("There is no Rank with the id = " + id);
    }
}