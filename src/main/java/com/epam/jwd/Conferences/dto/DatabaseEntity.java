package com.epam.jwd.Conferences.dto;

import java.io.Serializable;

/**
 * Implements the DatabaseEntity.
 */
public interface DatabaseEntity<I extends Serializable> {

    /**
     * A Getter for the entities id.
     *
     * @return The entities id.
     */
    I getId();
}
