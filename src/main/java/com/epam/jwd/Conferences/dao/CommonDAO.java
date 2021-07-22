package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.DatabaseEntity;
import com.epam.jwd.Conferences.exception.EntityNotFoundException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Communicates with DB and executes queries concerning to all the {@link T} entities.
 * It is the part (abstract class) of the Skeletal implementation.
 */
public abstract class CommonDAO<T extends DatabaseEntity<Long>> implements DAO<T, Long> {

    private static final Logger logger = LogManager.getLogger(CommonDAO.class);

    private static final String FIND_ALL_SQL_TEMPLATE = "select * from %s";
    private static final String FIND_BY_ID_SQL_TEMPLATE = "select * from %s where id = ?";
    private static final String DELETE_ENTITY_BY_ID_SQL_TEMPLATE = "delete from %s where id = ?";

    private final String tableName;
    private final String[] tableColumns;
    private final String findAllSql;
    private final String findByIdSql;
    private final String saveEntitySql;
    private final String deleteEntitySql;
    private final String updateEntitySql;

    protected CommonDAO(String tableName, String[] tableColumns) {
        this.tableName = tableName;
        this.tableColumns = tableColumns;
        this.findAllSql = String.format(FIND_ALL_SQL_TEMPLATE, tableName);
        this.findByIdSql = String.format(FIND_BY_ID_SQL_TEMPLATE, tableName);
        this.saveEntitySql = getSaveEntitySqlByTableNameAndTableColumnNames(tableName, tableColumns);
        this.deleteEntitySql = String.format(DELETE_ENTITY_BY_ID_SQL_TEMPLATE, tableName);
        this.updateEntitySql = getUpdateEntitySql();
    }

    private String getSaveEntitySqlByTableNameAndTableColumnNames(String tableName, String[] tableColumns) {
        StringBuilder saveEntitySql = new StringBuilder("insert into %s (");
        String[] tableColumnsToQuery = new String[tableColumns.length - 1];

        // copying the table columns without id column because by save id is defined by database
        System.arraycopy(tableColumns, 1, tableColumnsToQuery, 0, tableColumns.length - 1);
        saveEntitySql.append(String.join(", ", tableColumnsToQuery));
        saveEntitySql.append(") values (");
        String[] secondPartOfQuery = new String[tableColumns.length - 1];

        for(int i = 0; i < tableColumns.length - 1; i++) {
            secondPartOfQuery[i] = "?";
        }
        saveEntitySql.append(String.join(",", secondPartOfQuery));
        saveEntitySql.append(")");

        return String.valueOf(saveEntitySql);

//        switch (tableColumns.length) {
//            case 2:
//                return String.format("insert into %s (%s) values (?)", tableName, tableColumns[1]);
//            case 3:
//                return String.format("insert into %s (%s, %s) values (?, ?)",
//                        tableName, tableColumns[1], tableColumns[2]);
//            case 4:
//                return String.format("insert into %s (%s, %s, %s) values (?, ?, ?)",
//                        tableName, tableColumns[1], tableColumns[2], tableColumns[3]);
//            case 5:
//                return String.format("insert into %s (%s, %s, %s, %s) values (?, ?, ?, ?)",
//                        tableName, tableColumns[1], tableColumns[2], tableColumns[3], tableColumns[4]);
//            case 6:
//                return String.format("insert into %s (%s, %s, %s, %s, %s) values (?, ?, ?, ?, ?)",
//                        tableName, tableColumns[1],
//                        tableColumns[2], tableColumns[3], tableColumns[4], tableColumns[5]);
//            case 7:
//                return String.format("insert into %s (%s, %s, %s, %s, %s, %s) values (?, ?, ?, ?, ?, ?)",
//                        tableName, tableColumns[1],
//                        tableColumns[2], tableColumns[3],
//                        tableColumns[4], tableColumns[5], tableColumns[6]);
//            case 8:
//                return String.format("insert into %s (%s, %s, %s, %s, %s, %s, %s) values (?, ?, ?, ?, ?, ?, ?)",
//                        tableName, tableColumns[1],
//                        tableColumns[2], tableColumns[3],
//                        tableColumns[4], tableColumns[5],
//                        tableColumns[6], tableColumns[7]);
//            case 9:
//                return String.format("insert into %s (%s, %s, %s, %s, %s, %s, %s, %s)" +
//                                " values (?, ?, ?, ?, ?, ?, ?, ?)",
//                        tableName, tableColumns[1],
//                        tableColumns[2], tableColumns[3],
//                        tableColumns[4], tableColumns[5],
//                        tableColumns[6], tableColumns[7], tableColumns[8]);
//            case 10:
//                return String.format("insert into %s (%s, %s, %s, %s, %s, %s, %s, %s, %s)" +
//                                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                        tableName, tableColumns[1],
//                        tableColumns[2], tableColumns[3],
//                        tableColumns[4], tableColumns[5],
//                        tableColumns[6], tableColumns[7],
//                        tableColumns[8], tableColumns[9]);
//            case 11:
//                return String.format("insert into %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)" +
//                                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                        tableName, tableColumns[1],
//                        tableColumns[2], tableColumns[3],
//                        tableColumns[4], tableColumns[5],
//                        tableColumns[6], tableColumns[7],
//                        tableColumns[8], tableColumns[9], tableColumns[10]);
//            default:
//                return null;
//        }
    }

    /**
     * @return the tableName.
     */
    public String getTableName() {
        return tableName;
    }


    /**
     * Saves the {@link T} entity in the database.
     *
     * @param entity The {@link T} entity to save.
     */
    @Override
    public void save(T entity) {
        saveEntity(saveEntitySql, entity);
    }

    private void saveEntity(String saveEntitySql, T entity) {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             PreparedStatement statement = conn.prepareStatement(saveEntitySql)) {
            updateStatementBySave(statement, entity);
            statement.executeUpdate();
            logger.info("entity data was inserted");
        } catch (SQLException e) {
            logger.error(String.format("User name read unsuccessfully. The SQLState is '%s'", e.getSQLState()));
        } catch (InterruptedException exception) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public List<T> findAll() {
        return findEntities(findAllSql);
    }

    /**
     * Finds the {@link T} entity by id.
     *
     * @param id The id of the {@link T} entity to find.
     * @return {@link T}.
     */
    @Override
    public Optional<T> findById(Long id) {
        return takeFirstNotNull(
                findPreparedEntities(statement -> statement.setLong(1, id),
                        findByIdSql));
    }

    /**
     * Updates the {@link T} entity.
     *
     * @param entity The entity to update.
     */
    @Override
    public void update(T entity) {
        updateEntity(updateEntitySql, entity);
    }

    private void updateEntity(String updateEntitySql, T entity) {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             PreparedStatement statement = conn.prepareStatement(updateEntitySql)) {
            updateStatementByUpdate(statement, entity);
            statement.executeUpdate();
            logger.info("entity data was updated");
        } catch (SQLException e) {
            logger.error(String.format("Update was unsuccessfully. The SQLState is '%s'", e.getSQLState()));
        } catch (InterruptedException exception) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Deletes the {@link T} entity by id.
     *
     * @param id The id of the entity to delete.
     */
    @Override
    public void delete(Long id) throws EntityNotFoundException {
        deletePreparedEntity(preparedStatement -> preparedStatement.setLong(1, id), deleteEntitySql);
    }

    protected void deletePreparedEntity(SqlThrowingConsumer<PreparedStatement> preparationConsumer,
                                        String sql) throws EntityNotFoundException {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            preparationConsumer.accept(statement);
            try {
                int rowAffected = statement.executeUpdate();
                if (rowAffected < 1) {
                    logger.info("An attempt to delete non existing user was made.");
                    throw new EntityNotFoundException("An attempt to delete non existing user was made.");
                }
            } catch (SQLException exception) {
                logger.error(String.format("Something went wrong by the deleting the entity. The SQL State is '%s'.",
                        exception.getSQLState()));
            }
            logger.info("DB was red successfully and the entity was deleted successfully.");
        } catch (SQLException e) {
            logger.error(String.format("DB was red unsuccessfully. The SQL State is '%s'.", e.getSQLState()));
        } catch (InterruptedException exception) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    protected void deleteEntity(String deleteEntitySql, Long id) {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(deleteEntitySql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            logger.info(String.format("entity from Table '%s' with id '%s' deleted successfully", tableName, id));
        } catch (SQLException e) {
            logger.error(String.format("entity from table '%s' with id '%s' delete unsuccessfully." +
                    " The SQL state is '%s'.", tableName, id, e.getSQLState()));
        } catch (InterruptedException exception) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    protected List<T> findPreparedEntities(SqlThrowingConsumer<PreparedStatement> preparationConsumer,
                                           String sql) {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            preparationConsumer.accept(statement);
            try (final ResultSet resultSet = statement.executeQuery()) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                logger.info("DB was red successfully and list of entities was fetched.");
                return entities;
            }
        } catch (SQLException e) {
            logger.error(String.format("DB was red unsuccessfully. The SQL State is '%s'.", e.getSQLState()));
        } catch (InterruptedException exception) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + exception.getMessage());
            Thread.currentThread().interrupt();
        }
        return Collections.emptyList();
    }

    protected List<T> findEntities(String sql) {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             final Statement statement = conn.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery(sql)) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException e) {
            logger.error("user name read unsuccessfully. The SQL state is " + e.getSQLState()) ;
        } catch (InterruptedException exception) {
            logger.error("The thread "
                    + Thread.currentThread().getName()
                    + " was interrupted. "
                    + exception.getMessage());
            Thread.currentThread().interrupt();
        }
        return Collections.emptyList();
    }

    protected Optional<T> takeFirstNotNull(List<T> entities) {
        return entities.stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    protected abstract T mapResultSet(ResultSet resultSet) throws SQLException;

    protected abstract void updateStatementBySave(PreparedStatement statement, T entity);

    protected abstract String getUpdateEntitySql();

    protected abstract void updateStatementByUpdate(PreparedStatement statement, T entity);
}
