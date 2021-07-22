package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Conference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This is a DAO for the Conference entity. The singleton.
 */
public class DBConferenceDAO extends CommonDAO<Conference> implements ConferenceDAO {

    private static final Logger logger = LogManager.getLogger(DBConferenceDAO.class);

    private static final String ID_COLUMN = "id";
    private static final String CONFERENCE_TITLE_COLUMN = "conferenceTitle";
    private static final String MANAGER_COLUMN = "managerConf";
    private static final String TABLE_NAME = "conferences";
    private static final String[] columnNames = {ID_COLUMN, CONFERENCE_TITLE_COLUMN, MANAGER_COLUMN};

    private final String findByTitleSql;

    protected DBConferenceDAO(String tableName) {
        super(tableName, columnNames);
        findByTitleSql = String.format("select * from %s where %s = ?", TABLE_NAME, CONFERENCE_TITLE_COLUMN);
    }

    private static class DBConferenceDAOHolder {
        private final static DBConferenceDAO instance
                = new DBConferenceDAO(TABLE_NAME);
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static DBConferenceDAO getInstance() {
        return DBConferenceDAO.DBConferenceDAOHolder.instance;
    }

    @Override
    protected Conference mapResultSet(ResultSet resultSet) throws SQLException {
        return new Conference(resultSet.getLong(ID_COLUMN),
                resultSet.getString(CONFERENCE_TITLE_COLUMN),
                resultSet.getLong(MANAGER_COLUMN));
    }

    @Override
    protected void updateStatementBySave(PreparedStatement statement, Conference entity) {
        try {
            statement.setString(1, entity.getConferenceTitle());
            statement.setLong(2, entity.getManagerConf());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }
    }

    @Override
    protected String getUpdateEntitySql() {
        return "update "
                + getTableName()
                + " set "
                + CONFERENCE_TITLE_COLUMN
                + " = ?, "
                + MANAGER_COLUMN
                + " = ?"
                + " where "
                + ID_COLUMN
                + " = ?";
    }

    @Override
    protected void updateStatementByUpdate(PreparedStatement statement, Conference entity) {
        try {
            statement.setString(1, entity.getConferenceTitle());
            statement.setLong(2, entity.getManagerConf());
            statement.setLong(3, entity.getId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }
    }

    @Override
    public Optional<Conference> findConferenceByTitle(String title) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setString(1, title), // 1 потому что это первый вопросик в запросе
                findByTitleSql)
        );
    }
}
