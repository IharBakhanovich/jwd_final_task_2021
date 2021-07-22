package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.DatabaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * The main interface of the DAO of the application,
 * that defines the main operation on the DB (CRUD).
 */
public interface DAO<T extends DatabaseEntity<I>, I extends Serializable> {

    void save (T entity);

    List<T> findAll();

    Optional<T> findById(I id);

    void update (T entity);

    void delete(I id);


}
