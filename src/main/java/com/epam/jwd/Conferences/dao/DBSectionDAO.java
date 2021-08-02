package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Section;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * This is a DAO for the Section entity. The singleton.
 */
public class DBSectionDAO extends CommonDAO<Section> implements SectionDAO {

    private static final Logger logger = LogManager.getLogger(DBSectionDAO.class);

    private static final String ID_COLUMN = "id";
    private static final String CONFERENCE_ID_COLUMN = "conferenceId";
    private static final String SECTION_NAME_COLUMN = "sectionName";
    private static final String MANAGER_SECT_COLUMN = "managerSect";
    private static final String TABLE_NAME_SECTIONS = "sections";
    private static final String[] SECTION_TABLE_COLUMN_NAMES
            = {ID_COLUMN, CONFERENCE_ID_COLUMN, SECTION_NAME_COLUMN, MANAGER_SECT_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO = "select * from %s where %s = ?";

    private final String findByTitleSql;
    private final String findAllSectionsByConferenceIdSql;


    protected DBSectionDAO(String tableName) {
        super(tableName, SECTION_TABLE_COLUMN_NAMES);
        findByTitleSql = String.format(SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO, TABLE_NAME_SECTIONS, SECTION_NAME_COLUMN);
        findAllSectionsByConferenceIdSql = String.format(SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO, TABLE_NAME_SECTIONS, CONFERENCE_ID_COLUMN);
    }

    private static class DBSectionDAOHolder {
        private final static DBSectionDAO instance
                = new DBSectionDAO(TABLE_NAME_SECTIONS);
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static DBSectionDAO getInstance() {
        return DBSectionDAO.DBSectionDAOHolder.instance;
    }

    @Override
    protected Section mapResultSet(ResultSet resultSet) throws SQLException {
        return new Section(resultSet.getLong(ID_COLUMN),
                resultSet.getLong(CONFERENCE_ID_COLUMN),
                resultSet.getString(SECTION_NAME_COLUMN),
                resultSet.getLong(MANAGER_SECT_COLUMN));
    }

    @Override
    protected void updateStatementBySave(PreparedStatement statement, Section entity) {
        try {
            statement.setLong(1, entity.getConferenceId());
            statement.setString(2, entity.getSectionName());
            statement.setLong(3, entity.getManagerSect());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }
    }

    @Override
    protected String getUpdateEntitySql() {
        return "update "
                + getTableName()
                + " set "
                + CONFERENCE_ID_COLUMN
                + " = ?, "
                + SECTION_NAME_COLUMN
                + " = ?, "
                + MANAGER_SECT_COLUMN
                + " = ?"
                + " where "
                + ID_COLUMN
                + " = ?";
    }

    @Override
    protected void updateStatementByUpdate(PreparedStatement statement, Section entity) {
        try {
            statement.setLong(1, entity.getConferenceId());
            statement.setString(2, entity.getSectionName());
            statement.setLong(3, entity.getManagerSect());
            statement.setLong(4, entity.getId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }
    }

    /**
     * Returns the {@link Section} by the title.
     *
     * @param title The title of the returned {@link Section}.
     * @return {@link Section}.
     */
    @Override
    public Optional<Section> findSectionByTitle(String title) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setString(1, title), // 1 потому что это первый вопросик в запросе
                findByTitleSql)
        );
    }

    @Override
    public List<Section> findAllSectionsByConferenceID(Long id) {
        return findPreparedEntities(
                statement -> statement.setLong(1, id), findAllSectionsByConferenceIdSql
        );
    }
}
