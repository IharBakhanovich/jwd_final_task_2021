package com.epam.jwd.Conferences.dao;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlThrowingFunction<T, R> {

    R apply(T t) throws SQLException;

}
