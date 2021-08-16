package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.DatabaseEntity;
import com.epam.jwd.Conferences.exception.DatabaseException;
import com.epam.jwd.Conferences.exception.DuplicateException;
import com.epam.jwd.Conferences.exception.EntityNotFoundException;
import com.epam.jwd.Conferences.exception.NoConnectionException;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

/**
 * Communicates with DB and executes queries concerning to all the {@link T} entities.
 * It is the part (abstract class) of the Skeletal implementation.
 */
public abstract class CommonDAO<T extends DatabaseEntity<Long>> implements DAO<T, Long> {

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_COMMON_DAO; //LogManager.getLogger(CommonDAO.class);

//    private static final String FIND_ALL_SQL_TEMPLATE = "select * from %s";
//    private static final String FIND_BY_ID_SQL_TEMPLATE = "select * from %s where id = ?";
//    private static final String DELETE_ENTITY_BY_ID_SQL_TEMPLATE = "delete from %s where id = ?";

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
        this.findAllSql = String.format(ApplicationConstants.FIND_ALL_SQL_TEMPLATE, tableName);
        this.findByIdSql = String.format(ApplicationConstants.FIND_BY_ID_SQL_TEMPLATE, tableName);
        this.saveEntitySql = getSaveEntitySqlByTableNameAndTableColumnNames(tableName, tableColumns);
        this.deleteEntitySql = String.format(ApplicationConstants.DELETE_ENTITY_BY_ID_SQL_TEMPLATE, tableName);
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

        for (int i = 0; i < tableColumns.length - 1; i++) {
            secondPartOfQuery[i] = "?";
        }
        saveEntitySql.append(String.join(",", secondPartOfQuery));
        saveEntitySql.append(")");

        return String.format(String.valueOf(saveEntitySql), tableName);
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
    public void save(T entity) throws DuplicateException {
        saveEntity(saveEntitySql, entity);
    }

    private void saveEntity(String saveEntitySql, T entity) throws DuplicateException {
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             PreparedStatement statement = conn.prepareStatement(saveEntitySql)) {
            updateStatementBySave(statement, entity);
            statement.executeUpdate();
            logger.info("Entity data was inserted");
        } catch (SQLException e) {
            String state = e.getSQLState();
            if (state.startsWith("08")) {
                logger.error("No Database Connection.");
                throw new NoConnectionException("No Connection.");
            } else if (state.equals("23505") || state.equals("23000")) {
                logger.error("An attempt at updating User data violated unique constraints.");
                throw new DuplicateException("Violation of unique constraints.");
            } else {
                logger.error(String.format("Entity was NOT saved. The SQLState is '%s'", e.getSQLState()));
                throw new DatabaseException(String.format("SQLException was caught with state: %s", state));
            }
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
     * Finds the Optional{@link T} entity by id.
     *
     * @param id The id of the {@link T} entity to find.
     * @return Optional{@link T}.
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
            logger.error("user name read unsuccessfully. The SQL state is " + e.getSQLState());
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