package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.DatabaseEntity;
import com.epam.jwd.Conferences.exception.DuplicateException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * The main interface of the DAO of the application,
 * that defines the main operation on the DB (CRUD).
 */
public interface DAO<T extends DatabaseEntity<I>, I extends Serializable> {

    /**
     * Saves {@link T} in the database.
     *
     * @param entity is the {@link T} to save.
     * @throws DuplicateException if a SQLException with the state 23505 or the state 23000 is thrown.
     */
    void save (T entity) throws DuplicateException;

    /**
     * Finds all {@link T} entity in the database.
     *
     * @return List of the {@link T} objects.
     */
    List<T> findAll();

    /**
     * Finds {@link Optional<T>} in the database by the id of the {@link T}.
     *
     * @param id is the {@link I} to find.
     * @return {@link Optional<T>}.
     */
    Optional<T> findById(I id);

    /**
     * Updates the {@link T}.
     *
     * @param entity is the value of the {@link T} to update.
     */
    void update (T entity);

    /**
     * Deletes the {@link T} object from the database.
     *
     * @param id is the value of the {@link I} to find.
     */
    void delete(I id);
}