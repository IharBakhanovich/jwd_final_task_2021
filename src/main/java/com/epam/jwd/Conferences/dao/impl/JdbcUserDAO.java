package com.epam.jwd.Conferences.dao.impl;

import com.epam.jwd.Conferences.constants.ApplicationConstants;
import com.epam.jwd.Conferences.dao.UserDAO;
import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.User;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This is a DAO for the User entity. The singleton.
 */
public class JdbcUserDAO extends CommonDAO<User> implements UserDAO {

    private static final Logger logger = ApplicationConstants.LOGGER_FOR_DB_USER_DAO; //LogManager.getLogger(JdbcUserDAO.class);

//    private static final String ID_COLUMN = "id";
//    private static final String EMAIL_COLUMN = "email";
//    private static final String PASSWORD_COLUMN = "password";
//    private static final String SALT_COLUMN = "salt";
//    private static final String NUMBER_LOGIN_ATTEMPTS_COLUMN = "numberLoginAttempts";
//    private static final String VERIFICATION_TOKEN_COLUMN = "verificationToken";
//    private static final String EMAIL_VERIFIED_COLUMN = "emailVerified";
//    private static final String NICK_NAME_COLUMN = "nickname";
//    private static final String FIRST_NAME_COLUMN = "firstName";
//    private static final String SURNAME_COLUMN = "surname";
//    private static final String ROLE_COLUMN = "role";
//
//    private static final String TABLE_NAME = "users";
//    private static final String[] USER_TABLE_COLUMN_NAMES
//            = {ID_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN,
//            SALT_COLUMN, NUMBER_LOGIN_ATTEMPTS_COLUMN, VERIFICATION_TOKEN_COLUMN,
//            EMAIL_VERIFIED_COLUMN, NICK_NAME_COLUMN, FIRST_NAME_COLUMN,
//            SURNAME_COLUMN, ROLE_COLUMN};
//    public static final String SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_USER_DAO = "select * from %s where %s = ?";

    private final String findByNicknameSql;

    protected JdbcUserDAO() {
        super(ApplicationConstants.TABLE_NAME, ApplicationConstants.USER_TABLE_COLUMN_NAMES);
        this.findByNicknameSql = String.format(ApplicationConstants.SELECT_ALL_FROM_TABLE_BY_COLUMN_FOR_DB_USER_DAO,
                ApplicationConstants.TABLE_NAME, ApplicationConstants.NICK_NAME_COLUMN);
    }

    private static class JdbcUserDAOHolder {
        private final static JdbcUserDAO instance
                = new JdbcUserDAO();
    }

    /**
     * Returns the instance of this class.
     *
     * @return Object of this class.
     */
    public static JdbcUserDAO getInstance() {
        return JdbcUserDAO.JdbcUserDAOHolder.instance;
    }

    @Override
    protected User mapResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong(ApplicationConstants.ID_COLUMN),
                resultSet.getString(ApplicationConstants.EMAIL_COLUMN),
                resultSet.getString(ApplicationConstants.PASSWORD_COLUMN),
                resultSet.getString(ApplicationConstants.SALT_COLUMN),
                resultSet.getInt(ApplicationConstants.NUMBER_LOGIN_ATTEMPTS_COLUMN),
                resultSet.getString(ApplicationConstants.VERIFICATION_TOKEN_COLUMN),
                resultSet.getBoolean(ApplicationConstants.EMAIL_VERIFIED_COLUMN),
                resultSet.getString(ApplicationConstants.NICK_NAME_COLUMN),
                resultSet.getString(ApplicationConstants.FIRST_NAME_COLUMN),
                resultSet.getString(ApplicationConstants.SURNAME_COLUMN),
                Role.resolveRoleById(resultSet.getLong(ApplicationConstants.ROLE_COLUMN)));
    }

    @Override
    protected void updateStatementBySave(PreparedStatement statement, User entity) {
        try {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getSalt());
            statement.setInt(4, entity.getNumberLoginAttempts());
            statement.setString(5, entity.getVerificationToken());
            statement.setBoolean(6, entity.isEmailVerified());
            statement.setString(7, entity.getNickname());
            statement.setString(8, entity.getFirstName());
            statement.setString(9, entity.getSurname());
            statement.setLong(10, entity.getRole().getId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }
    }

    @Override
    protected String getUpdateEntitySql() {
        return "update "
                + getTableName()
                + " set "
                + ApplicationConstants.EMAIL_COLUMN
                + " = ?, "
                + ApplicationConstants.NICK_NAME_COLUMN
                + " = ?, "
                + ApplicationConstants.FIRST_NAME_COLUMN
                + " = ?, "
                + ApplicationConstants.SURNAME_COLUMN
                + " = ?, "
                + ApplicationConstants.ROLE_COLUMN
                + " = ?"
                + " where "
                + ApplicationConstants.ID_COLUMN
                + " = ?";
    }

    @Override
    protected void updateStatementByUpdate(PreparedStatement statement, User entity) {
        try {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getNickname());
            statement.setString(3, entity.getFirstName());
            statement.setString(4, entity.getSurname());
            statement.setLong(5, entity.getRole().getId());
            statement.setLong(6, entity.getId());
        } catch (SQLException e) {
            logger.error("Error by updating the statement. The SQLState is " + e.getSQLState());
        }

    }

    /**
     * Finds {@link Optional<User>} by the parameter nickname of {@link User}, which has the value
     * equals to the {@param nickname}.
     *
     * @param nickname is the {@link String} which is the nickname to search.
     * @return {@link Optional<User>} by the parameter nickname of {@link User}, which has the value
     * equals to the {@param nickname}.
     */
    @Override
    public Optional<User> findUserByNickname(String nickname) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setString(1, nickname), findByNicknameSql)
        );
    }

    /**
     * Updates {@link User}s parameter role.
     *
     * @param userId  is the id of the {@link User} which role is to update.
     * @param newRole is the value of the new role of the {@link User} with the id equals {@param userId}.
     */
    @Override
    public void updateUserRoleByUserId(Long userId, Long newRole) {
        String updateUserRoleByUserIdSql = "update "
                + ApplicationConstants.TABLE_NAME
                + " set "
                + ApplicationConstants.ROLE_COLUMN
                + " = " + newRole
                + " where "
                + ApplicationConstants.ID_COLUMN
                + " = " + userId;
        try (final Connection conn = ConnectionPool.retrieve().takeConnection();
             PreparedStatement statement = conn.prepareStatement(updateUserRoleByUserIdSql)) {
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
}
