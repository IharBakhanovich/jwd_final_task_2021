package com.epam.jwd.Conferences.dto;

import com.epam.jwd.Conferences.exception.UnknownEntityException;

import java.util.Arrays;
import java.util.List;

/**
 * Stores the enum Role.
 */
public enum Role implements DatabaseEntity<Long> {
    USER(1L),
    ADMIN(2L),
    MANAGER(3L),
    UNAUTHORIZED(4l);

    private final Long id;

    /**
     * Constructs a new {@link Role}.
     *
     * @param id is the value of the id of the new {@link Role}
     */
    Role(Long id) {
        this.id = id;
    }

    /**
     * creates the {@link List<Role>} with all the values of the {@link Role}.
     */
    public static final List<Role> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    /**
     * Returns the List of all the Role values.
     *
     * @return {@link List<Role>}.
     */
    public static List<Role> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }


    /**
     * A Getter for the name.
     *
     * @return The name
     */
    public String getName() {
        return this.name();
    }

    /**
     * Returns Role by id.
     *
     * @param id is the id to search.
     * @return {@link Role} with the id equals the {@param id}.
     * @throws UnknownEntityException if such id does not exist.
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return id;
    }
}
