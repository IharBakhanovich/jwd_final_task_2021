package com.epam.jwd.Conferences.dao.impl;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dao.SectionDAO;
import com.epam.jwd.Conferences.dto.Section;
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

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_DB_SECTION_DAO; //LogManager.getLogger(DBSectionDAO.class);

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
    private final String findAllSectionsWhereUserIsManagerSql;


    protected DBSectionDAO(String tableName) {
        super(tableName, ApplicationConstants.SECTION_TABLE_COLUMN_NAMES);
        findByTitleSql = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO,
                ApplicationConstants.TABLE_NAME_SECTIONS, ApplicationConstants.SECTION_NAME_COLUMN);
        findAllSectionsByConferenceIdSql
                = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_SECTION_DAO,
                ApplicationConstants.TABLE_NAME_SECTIONS, ApplicationConstants.CONFERENCE_ID_COLUMN);
        findAllSectionsWhereUserIsManagerSql
                = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL,
                ApplicationConstants.TABLE_NAME_SECTIONS, ApplicationConstants.MANAGER_SECT_COLUMN);
    }

    private static class DBSectionDAOHolder {
        private final static DBSectionDAO instance
                = new DBSectionDAO(ApplicationConstants.TABLE_NAME_SECTIONS);
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
        return new Section(resultSet.getLong(ApplicationConstants.ID_COLUMN),
                resultSet.getLong(ApplicationConstants.CONFERENCE_ID_COLUMN),
                resultSet.getString(ApplicationConstants.SECTION_NAME_COLUMN),
                resultSet.getLong(ApplicationConstants.MANAGER_SECT_COLUMN));
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
                + ApplicationConstants.CONFERENCE_ID_COLUMN
                + " = ?, "
                + ApplicationConstants.SECTION_NAME_COLUMN
                + " = ?, "
                + ApplicationConstants.MANAGER_SECT_COLUMN
                + " = ?"
                + " where "
                + ApplicationConstants.ID_COLUMN
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
     * Finds {@link Optional<Section>} by the value of the {@link Section}s sectionName parameter.
     *
     * @param title is the value of the of the {@link Section}s sectionName parameter to search.
     * @return {@link Optional<Section>} which parameter sectionName equals to the calue of the {@param title}.
     */
    @Override
    public Optional<Section> findSectionByTitle(String title) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setString(1, title), // 1 потому что это первый вопросик в запросе
                findByTitleSql)
        );
    }

    /**
     * Finds all the sections of the {@link com.epam.jwd.Conferences.dto.Conference} with the conferenceId
     * equals to the {@param id}.
     *
     * @param id is the value of the {@link com.epam.jwd.Conferences.dto.Conference}s id parameter,
     *           which sections to find
     * @return {@link List<Section>} with all the {@link Section}s of the conference with the id
     * equals to the {@param id}.
     */
    @Override
    public List<Section> findAllSectionsByConferenceID(Long id) {
        return findPreparedEntities(
                statement -> statement.setLong(1, id), findAllSectionsByConferenceIdSql
        );
    }

    /**
     * Finds all {@link Section}s in the database with the parameter managerSect equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Section}s parameter managerSect to find.
     * @return {@link List<Section>} that contains all the {@link Section}s in the database with the parameter
     * managerSect equals {@param userId}.
     */
    @Override
    public List<Section> findAllSectionsWhereUserIsManager(Long userId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, userId),
                findAllSectionsWhereUserIsManagerSql);
    }
}