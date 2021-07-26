package com.epam.jwd.Conferences.dao;

import com.epam.jwd.Conferences.dto.Role;
import com.epam.jwd.Conferences.dto.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This is a DAO for the User entity. The singleton.
 */
public class JdbcUserDAO extends CommonDAO<User> implements UserDAO {

    private static final Logger logger = LogManager.getLogger(JdbcUserDAO.class);

    private static final String ID_COLUMN = "id";
    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    private static final String SALT_COLUMN = "salt";
    private static final String NUMBER_LOGIN_ATTEMPTS_COLUMN = "numberLoginAttempts";
    private static final String VERIFICATION_TOKEN_COLUMN = "verificationToken";
    private static final String EMAIL_VERIFIED_COLUMN = "emailVerified";
    private static final String NICK_NAME_COLUMN = "nickname";
    private static final String FIRST_NAME_COLUMN = "firstName";
    private static final String SURNAME_COLUMN = "surname";
    private static final String ROLE_COLUMN = "role";

    private static final String TABLE_NAME = "users";
    private static final String[] columnNames
            = {ID_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN,
            SALT_COLUMN, NUMBER_LOGIN_ATTEMPTS_COLUMN, VERIFICATION_TOKEN_COLUMN,
            EMAIL_VERIFIED_COLUMN, NICK_NAME_COLUMN, FIRST_NAME_COLUMN,
            SURNAME_COLUMN, ROLE_COLUMN};

    private final String findByNicknameSql;

    protected JdbcUserDAO() {
        super(TABLE_NAME, columnNames);
        this.findByNicknameSql = String.format("select * from %s where %s = ?", TABLE_NAME, NICK_NAME_COLUMN);
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
        return new User(resultSet.getLong(ID_COLUMN),
                resultSet.getString(EMAIL_COLUMN),
                resultSet.getString(PASSWORD_COLUMN),
                resultSet.getString(SALT_COLUMN),
                resultSet.getInt(NUMBER_LOGIN_ATTEMPTS_COLUMN),
                resultSet.getString(VERIFICATION_TOKEN_COLUMN),
                resultSet.getBoolean(EMAIL_VERIFIED_COLUMN),
                resultSet.getString(NICK_NAME_COLUMN),
                resultSet.getString(FIRST_NAME_COLUMN),
                resultSet.getString(SURNAME_COLUMN),
                Role.resolveRoleById(resultSet.getLong(ROLE_COLUMN)));
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
                + EMAIL_COLUMN
                + " = ?, "
                + NICK_NAME_COLUMN
                + " = ?, "
                + FIRST_NAME_COLUMN
                + " = ?, "
                + SURNAME_COLUMN
                + " = ?, "
                + ROLE_COLUMN
                + " = ?"
                + " where "
                + ID_COLUMN
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

    @Override
    public Optional<User> findUserByNickname(String nickname) {
        return takeFirstNotNull(findPreparedEntities(
                statement -> statement.setString(1, nickname), findByNicknameSql)
        );
    }
}
