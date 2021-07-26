package com.epam.jwd.Conferences.dto;

import com.epam.jwd.Conferences.exception.UnknownEntityException;

import java.util.Arrays;
import java.util.List;

public enum ReportType implements DatabaseEntity<Long> {
    QUESTION(1L),
    APPLICATION(2L);

    private final Long id;

    ReportType(Long id) {
        this.id = id;
    }

    //сдвигаем создание листа в этот класс, чтобы каждый раз не создавать лист из всех ролей в классе AppCommand.
    public static final List<ReportType> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    // этот лист будет всегда возвращаться в единственном экземпляре, до этого единожды создавшись
    public static List<ReportType> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
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
