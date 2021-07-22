package com.epam.jwd.Conferences.dto;

import com.epam.jwd.Conferences.exception.UnknownEntityException;

public enum ReportType implements DatabaseEntity<Long> {
    QUESTION(1L),
    APPLICATION(2L);

    private final Long id;

    ReportType(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * todo via java.lang.enum methods!
     */
    public String getName() {
        return this.name();
    }

    /**
     * todo via java.lang.enum methods!
     *
     * @throws UnknownEntityException if such id does not exist
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
