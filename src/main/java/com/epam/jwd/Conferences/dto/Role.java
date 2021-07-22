package com.epam.jwd.Conferences.dto;

import com.epam.jwd.Conferences.exception.UnknownEntityException;

import java.util.Arrays;
import java.util.List;

public enum Role implements DatabaseEntity<Long> {
    USER(1L),
    ADMIN(2L),
    MANAGER(3L),
    UNAUTHORIZED(4l);

    private final Long id;

    Role(Long id) {
        this.id = id;
    }

    //сдвигаем создание листа в этот класс, чтобы каждый раз не создавать лист из всех ролей в классе AppCommand.
    public static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    // этот лист будет всегда возвращаться в единственном экземпляре, до этого единожды создавшись
    public static List<Role> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }


    /**
     *
     */
    public String getName() {
        return this.name();
    }

    /**
     * @throws UnknownEntityException if such id does not exist
     */
    public static Role resolveRoleById(Long id) {
        for (Role role : Role.values()
        ) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        throw new UnknownEntityException("There is no Rank with the id = " + id);
    }

    @Override
    public Long getId() {
        return id;
    }
}
