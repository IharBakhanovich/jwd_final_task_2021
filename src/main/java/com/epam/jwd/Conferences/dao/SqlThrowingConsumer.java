package com.epam.jwd.Conferences.dao;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlThrowingConsumer<T> {

    void accept(T t) throws SQLException;

}
