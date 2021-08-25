package com.epam.jwd.Conferences.dao.impl;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dao.ReportDAO;
import com.epam.jwd.Conferences.dto.Report;
import com.epam.jwd.Conferences.dto.ReportType;
import com.epam.jwd.Conferences.dto.User;
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

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_DB_REPORT_DAO; //LogManager.getLogger(DBSectionDAO.class);

//    private static final String ID_COLUMN = "id";
//    private static final String SECTION_ID_COLUMN = "sectionId";
//    private static final String CONFERENCE_ID_COLUMN = "conferenceId";
//    private static final String REPORT_TEXT_COLUMN = "reportText";
//    private static final String REPORT_TYPE_COLUMN = "reportType";
//    private static final String APPLICANT_COLUMN = "applicant";
//    private static final String TABLE_NAME_REPORTS = "reports";
//    private static final String QUESTION_REPORT_ID_COLUMN = "questionReportId";
//    private static final String[] REPORT_TABLE_COLUMN_NAMES
//            = {ApplicationConstants.ID_COLUMN, ApplicationConstants.SECTION_ID_COLUMN,
//            ApplicationConstants.CONFERENCE_ID_COLUMN, ApplicationConstants.REPORT_TEXT_COLUMN,
//            ApplicationConstants.REPORT_TYPE_COLUMN, ApplicationConstants.APPLICANT_COLUMN,
//            ApplicationConstants.QUESTION_REPORT_ID_COLUMN};
//    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL
//            = "select * from %s where %s = ?";
//    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_ONE_AND_COLUMN_TWO_FOR_DB_REPORT_DAO_SQL
//            = "select * from %s where %s = ? and %s = ?";
//    public static final String SELECT_ALL_QUESTION_BY_MANAGER_ID_SQL
//            = "select %s.* from %s, %s where %s.%s = 1 and %s.%s = %s.%s and %s.%s = ?";
//    public static final String SELECT_ALL_APPLICATIONS_BY_MANAGER_ID_SQL
//            = "select %s.* from %s, %s where %s.%s = 2 and %s.%s = %s.%s and %s.%s = ?";
//    public static final String SELECT_ALL_QUESTION_BY_APPLICANT_ID_SQL
//            = "select * from %s where %s = 1 and %s = ?";
//    public static final String SELECT_ALL_APPLICATIONS_BY_APPLICANT_ID_SQL
//            = "select * from %s where %s = 2 and %s = ?";
//
//    private static final String MANAGER_SECT_COLUMN = "managerSect";
//    private static final String TABLE_NAME_SECTIONS = "sections";

    private final String findReportByApplicantIdSql;
    private final String findAllReportsByConferenceIdAndSectionIdSql;
    private final String findAllQuestionsByManagerIdSql;
    private final String findAllReportsByQuestionReportIdSql;
    private final String findAllQuestionsByApplicantIdSql;
    private final String findAllApplicationsByManagerIdSql;
    private final String findAllApplicationsByApplicantIdSql;
    private final String findAllReportsByUserIdSql;

    protected DBReportDAO(String tableName) {
        super(tableName, ApplicationConstants.REPORT_TABLE_COLUMN_NAMES);
        findReportByApplicantIdSql = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.APPLICANT_COLUMN);
        findAllReportsByConferenceIdAndSectionIdSql = String.format(
                ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_ONE_AND_COLUMN_TWO_FOR_DB_REPORT_DAO_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.SECTION_ID_COLUMN,
                ApplicationConstants.CONFERENCE_ID_COLUMN);
        findAllQuestionsByManagerIdSql = String.format(ApplicationConstants.SELECT_ALL_QUESTION_BY_MANAGER_ID_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.TABLE_NAME_SECTIONS, ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.REPORT_TYPE_COLUMN, ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.SECTION_ID_COLUMN, ApplicationConstants.TABLE_NAME_SECTIONS,
                ApplicationConstants.ID_COLUMN, ApplicationConstants.TABLE_NAME_SECTIONS,
                ApplicationConstants.MANAGER_SECT_COLUMN);
        findAllReportsByQuestionReportIdSql
                = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.QUESTION_REPORT_ID_COLUMN);
        findAllQuestionsByApplicantIdSql = String.format(ApplicationConstants.SELECT_ALL_QUESTION_BY_APPLICANT_ID_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.REPORT_TYPE_COLUMN,
                ApplicationConstants.APPLICANT_COLUMN);
        findAllApplicationsByApplicantIdSql = String.format(ApplicationConstants.SELECT_ALL_APPLICATIONS_BY_APPLICANT_ID_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.REPORT_TYPE_COLUMN,
                ApplicationConstants.APPLICANT_COLUMN);
        findAllApplicationsByManagerIdSql
                = String.format(ApplicationConstants.SELECT_ALL_APPLICATIONS_BY_MANAGER_ID_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.TABLE_NAME_SECTIONS, ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.REPORT_TYPE_COLUMN, ApplicationConstants.TABLE_NAME_REPORTS,
                ApplicationConstants.SECTION_ID_COLUMN, ApplicationConstants.TABLE_NAME_SECTIONS,
                ApplicationConstants.ID_COLUMN, ApplicationConstants.TABLE_NAME_SECTIONS,
                ApplicationConstants.MANAGER_SECT_COLUMN);
        findAllReportsByUserIdSql = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_REPORT_DAO_SQL,
                ApplicationConstants.TABLE_NAME_REPORTS, ApplicationConstants.APPLICANT_COLUMN);
    }

    private static class DBReportDAOHolder {
        private final static DBReportDAO instance
                = new DBReportDAO(ApplicationConstants.TABLE_NAME_REPORTS);
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
        return new Report(resultSet.getLong(ApplicationConstants.ID_COLUMN),
                resultSet.getLong(ApplicationConstants.SECTION_ID_COLUMN),
                resultSet.getLong(ApplicationConstants.CONFERENCE_ID_COLUMN),
                resultSet.getString(ApplicationConstants.REPORT_TEXT_COLUMN),
                ReportType.resolveReportTypeById(resultSet.getLong(ApplicationConstants.REPORT_TYPE_COLUMN)),
                resultSet.getLong(ApplicationConstants.APPLICANT_COLUMN),
                resultSet.getLong(ApplicationConstants.QUESTION_REPORT_ID_COLUMN));
    }

    @Override
    protected void updateStatementBySave(PreparedStatement statement, Report entity) {
        try {
            statement.setLong(1, entity.getSectionId());
            statement.setLong(2, entity.getConferenceId());
            statement.setString(3, entity.getReportText());
            statement.setLong(4, entity.getReportType().getId());
            statement.setLong(5, entity.getApplicant());
            statement.setLong(6, entity.getQuestionReportId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }

    }

    @Override
    protected String getUpdateEntitySql() {
        return "update "
                + getTableName()
                + " set "
                + ApplicationConstants.SECTION_ID_COLUMN
                + " = ?, "
                + ApplicationConstants.CONFERENCE_ID_COLUMN
                + " = ?, "
                + ApplicationConstants.REPORT_TEXT_COLUMN
                + " = ?, "
                + ApplicationConstants.REPORT_TYPE_COLUMN
                + " = ?, "
                + ApplicationConstants.APPLICANT_COLUMN
                + " = ?, "
                + ApplicationConstants.QUESTION_REPORT_ID_COLUMN
                + " = ?"
                + " where "
                + ApplicationConstants.ID_COLUMN
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
            statement.setLong(6, entity.getQuestionReportId());
            statement.setLong(7, entity.getId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }

    }

    /**
     * Finds the {@link Report} of the applicant.
     *
     * @param applicantId The Id of the applicant of the returned {@link Report} report.
     * @return {@link Report}.
     */
    @Override
    public Optional<Report> findReportByApplicantId(Long applicantId) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setLong(1, applicantId),
                findReportByApplicantIdSql)
        );
    }

    /**
     * Finds all {@link Report}s in the database by sectionId and conferenceId.
     *
     * @param sectionId is the {@link Long} fo the sectionId to find.
     * @param conferenceId is the {@link Long} fo the conferenceId to find.
     * @return {@link List<Report>} that contains all the reports
     *         in the {@param conferenceId}/{@param sectionId} section
     */
    @Override
    public List<Report> findAllReportsBySectionID(Long sectionId, Long conferenceId) {
        return findPreparedEntities(
                statement -> setParameters(statement, sectionId, conferenceId),
                findAllReportsByConferenceIdAndSectionIdSql
        );
    }

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.QUESTION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    @Override
    public List<Report> findAllQuestionsByManagerId(Long managerId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, managerId),
                findAllQuestionsByManagerIdSql);
    }

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.QUESTION, which were created by a {@link User} with the id equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} questions created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.QUESTION,
     * which are created by the {@link User} with id equals {@param managerId}.
     */
    @Override
    public List<Report> findAllQuestionsByApplicantId(Long managerId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, managerId),
                findAllQuestionsByApplicantIdSql);
    }

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which are inherited to the sections which are managed by the user with id
     * equals {@param managerId}.
     *
     * @param managerId is the {@link Long} that is id of the {@link User} which questions are found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.APPLICATION,
     * which are inherited to the sections which are managed by the user with id equals {@param managerId}.
     */
    @Override
    public List<Report> findAllApplicationsByManagerId(Long managerId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, managerId),
                findAllApplicationsByManagerIdSql);
    }

    /**
     * Finds all {@link Report}s in the database that have the {@link com.epam.jwd.Conferences.dto.ReportType} equals
     * ReportType.APPLICATION, which were created by a {@link User} with the id equals {@param applicantId}.
     *
     * @param applicantId is the {@link Long} that is id of the {@link User} application created by him to found.
     * @return {@link List<Report>} that contains all the {@link Report}s with the ReportType.APPLICATION,
     * which are created by the {@link User} with id equals {@param applicantId}.
     */
    @Override
    public List<Report> findAllApplicationsByApplicantId(Long applicantId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, applicantId),
                findAllApplicationsByApplicantIdSql);
    }

    /**
     * Finds all {@link Report}s in the database that have the parameter applicant equals {@param userId}.
     *
     * @param userId is the {@link Long} that value equals to value of {@link Report}s parameter applicant to find.
     * @return {@link List<Report>} that contains all the {@link Report}s in the database with the parameter applicant
     * equals {@param userId}.
     */
    @Override
    public List<Report> findAllReportsByUserId(Long userId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, userId),
                findAllReportsByUserIdSql);
    }

    /**
     * Finds all the {@link Report}s in the database that have the parameter questionReportId equals to the value
     * of the {@param questionReportId}
     * @param questionReportId is the {@link Long} value of the parameter questionReportId of the Report.
     * @return {@link List<Report>} that contains all the {@link Report}s with the value of the parameter
     * questionReportId equals to the {@param questionReportId}.
     */
    @Override
    public List<Report> findAllReportsByQuestionReportId(Long questionReportId) {
        return findPreparedEntities(
                statement -> statement.setLong(1, questionReportId),
                findAllReportsByQuestionReportIdSql);
    }

    private void setParameters(
            PreparedStatement preparedStatement, Long firstParameter, Long secondParameter) throws SQLException {
        preparedStatement.setLong(1, firstParameter);
        preparedStatement.setLong(2, secondParameter);
    }
}
