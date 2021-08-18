package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dto.Conference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * This is a DAO for the Conference entity. The singleton.
 */
public class DBConferenceDAO extends CommonDAO<Conference> implements ConferenceDAO {

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_DB_CONFERENCE_DAO; //LogManager.getLogger(DBConferenceDAO.class);

//    private static final String ID_COLUMN = "id";
//    private static final String CONFERENCE_TITLE_COLUMN = "conferenceTitle";
//    private static final String MANAGER_CONF_COLUMN = "managerConf";
//    private static final String TABLE_NAME_CONFERENCES = "conferences";
//    private static final String[] COLUMN_NAMES_FOR_DB_CONFERENCE_DAO = {ApplicationConstants.ID_COLUMN,
//            ApplicationConstants.CONFERENCE_TITLE_COLUMN, ApplicationConstants.MANAGER_CONF_COLUMN};
//    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_CONFERENCE_DAO = "select * from %s where %s = ?";

    private final String findByTitleSql;
    private final String findAllConferencesWhereUserIsManagerSql;

    protected DBConferenceDAO(String tableName) {
        super(tableName, ApplicationConstants.COLUMN_NAMES_FOR_DB_CONFERENCE_DAO);
        findByTitleSql = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_CONFERENCE_DAO,
                ApplicationConstants.TABLE_NAME_CONFERENCES, ApplicationConstants.CONFERENCE_TITLE_COLUMN);
        findAllConferencesWhereUserIsManagerSql
                = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL,
                ApplicationConstants.TABLE_NAME_CONFERENCES, ApplicationConstants.MANAGER_CONF_COLUMN);
    }

    private static class DBConferenceDAOHolder {
        private final static DBConferenceDAO instance
                = new DBConferenceDAO(ApplicationConstants.TABLE_NAME_CONFERENCES);
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
        return new Conference(resultSet.getLong(ApplicationConstants.ID_COLUMN),
                resultSet.getString(ApplicationConstants.CONFERENCE_TITLE_COLUMN),
                resultSet.getLong(ApplicationConstants.MANAGER_CONF_COLUMN));
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
                + ApplicationConstants.CONFERENCE_TITLE_COLUMN
                + " = ?, "
                + ApplicationConstants.MANAGER_CONF_COLUMN
                + " = ?"
                + " where "
                + ApplicationConstants.ID_COLUMN
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

    /**
     * Finds {@link Optional<Conference>} by the parameter conferenceTitle of the {@link Conference}.
     *
     * @param title is a String, that is a title of the Conference.
     * @return {@link Optional<Conference>}, that contains the {@link Conference} if it is exist in the system.
     */
    @Override
    public Optional<Conference> findConferenceByTitle(String title) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setString(1, title), // 1 потому что это первый вопросик в запросе
                findByTitleSql)
        );
    }

    /**
     * Finds all {@link Conference}s in the database with the parameter managerConf equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Conference}s parameter managerConf to find.
     * @return {@link List <Conference>} that contains all the {@link Conference}s in the database with the parameter
     * managerConf equals {@param userId}.
     */
    @Override
    public List<Conference> findAllConferencesWhereUserIsManager(Long userId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, userId),
                findAllConferencesWhereUserIsManagerSql);
    }
}
