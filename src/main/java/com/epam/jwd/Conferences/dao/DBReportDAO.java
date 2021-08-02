package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.ReportType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * This is a DAO for the Report table. The singleton.
 */
public class DBReportDAO extends CommonDAO<Report> implements ReportDAO {

    private static final Logger logger = LogManager.getLogger(DBSectionDAO.class);

    private static final String ID_COLUMN = "id";
    private static final String SECTION_ID_COLUMN = "sectionId";
    private static final String CONFERENCE_ID_COLUMN = "conferenceId";
    private static final String REPORT_TEXT_COLUMN = "reportText";
    private static final String REPORT_TYPE_COLUMN = "reportType";
    private static final String APPLICANT_COLUMN = "applicant";
    private static final String TABLE_NAME_REPORTS = "reports";
    private static final String[] REPORT_TABLE_COLUMN_NAMES
            = {ID_COLUMN, SECTION_ID_COLUMN, CONFERENCE_ID_COLUMN,
            REPORT_TEXT_COLUMN, REPORT_TYPE_COLUMN, APPLICANT_COLUMN};
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO = "select * from %s where %s = ?";
    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_ONE_AND_COLUMN_TWO_FOR_DB_REPORT_DAO = "select * from %s where %s = ? and %s = ?";

    private final String findReportByApplicantIdSql;
    private final String findAllReportsByConferenceIdAndSectionIdSql;

    protected DBReportDAO(String tableName) {
        super(tableName, REPORT_TABLE_COLUMN_NAMES);
        findReportByApplicantIdSql = String.format(SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO,
                TABLE_NAME_REPORTS, APPLICANT_COLUMN);
        findAllReportsByConferenceIdAndSectionIdSql = String.format(
                SELECT_ALL_FROM_TABLE_BY_COLUMN_ONE_AND_COLUMN_TWO_FOR_DB_REPORT_DAO,
                TABLE_NAME_REPORTS, SECTION_ID_COLUMN, CONFERENCE_ID_COLUMN);
    }

    private static class DBReportDAOHolder {
        private final static DBReportDAO instance
                = new DBReportDAO(TABLE_NAME_REPORTS);
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static DBReportDAO getInstance() {
        return DBReportDAO.DBReportDAOHolder.instance;
    }

    @Override
    protected Report mapResultSet(ResultSet resultSet) throws SQLException {
        return new Report(resultSet.getLong(ID_COLUMN),
                resultSet.getLong(SECTION_ID_COLUMN),
                resultSet.getLong(CONFERENCE_ID_COLUMN),
                resultSet.getString(REPORT_TEXT_COLUMN),
                ReportType.resolveReportTypeById(resultSet.getLong(REPORT_TYPE_COLUMN)),
                resultSet.getLong(APPLICANT_COLUMN));
    }

    @Override
    protected void updateStatementBySave(PreparedStatement statement, Report entity) {
        try {
            statement.setLong(1, entity.getSectionId());
            statement.setLong(2, entity.getConferenceId());
            statement.setString(3, entity.getReportText());
            statement.setLong(4, entity.getReportType().getId());
            statement.setLong(5, entity.getApplicant());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }

    }

    @Override
    protected String getUpdateEntitySql() {
        return "update "
                + getTableName()
                + " set "
                + SECTION_ID_COLUMN
                + " = ?, "
                + CONFERENCE_ID_COLUMN
                + " = ?, "
                + REPORT_TEXT_COLUMN
                + " = ?, "
                + REPORT_TYPE_COLUMN
                + " = ?, "
                + APPLICANT_COLUMN
                + " = ?"
                + " where "
                + ID_COLUMN
                + " = ?";
    }

    @Override
    protected void updateStatementByUpdate(PreparedStatement statement, Report entity) {
        try {
            statement.setLong(1, entity.getSectionId());
            statement.setLong(2, entity.getConferenceId());
            statement.setString(3, entity.getReportText());
            statement.setLong(4, entity.getReportType().getId());
            statement.setLong(5, entity.getApplicant());
            statement.setLong(6, entity.getId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }

    }

    /**
     * Finds the {@link Report} of the applicant.
     * @param applicantId	The Id of the applicant of the returned {@link Report} report.
     *
     * @return {@link Report}.
     */
    @Override
    public Optional<Report> findReportByApplicantId(Long applicantId) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setLong(1, applicantId),
                findReportByApplicantIdSql)
        );
    }

    @Override
    public List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId) {
        return findPreparedEntities(
                statement -> setParameters(statement, sectionId, conferenceId),
                findAllReportsByConferenceIdAndSectionIdSql
        );
    }

    private void setParameters(
            PreparedStatement preparedStatement, Long firstParameter, Long secondParameter) throws SQLException {
        preparedStatement.setLong(1, firstParameter);
        preparedStatement.setLong(2, secondParameter);
    }
}
