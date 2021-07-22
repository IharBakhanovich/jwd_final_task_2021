package com.epam.jwd.Conferences.app;

import com.epam.jwd.Conferences.exception.CouldNotInitializeConnectionPoolException;
import com.epam.jwd.Conferences.pool.AppConnectionPool;
import com.epam.jwd.Conferences.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;

public class Main {
    private static final Logger logger = LogManager.getLogger(AppConnectionPool.class);

    public static void main(String[] args) throws CouldNotInitializeConnectionPoolException {
//        logger.info("Initialising connection pool");
//        ConnectionPool.retrieve().init();
//        // TODO проверить работоспособность ConnectionPool

        String[] tableColumns1 = {"id", "sectionId", "conferenceId", "reportText", "reportType", "applicant"};
        String tableName1 = "reports";
        String[] tableColumns2 = {"id", "conferenceTitle", "managerConf"};
        String tableName2 = "conferences";
        String[] tableColumns3 = {"id", "conferenceId", "sectionName", "managerSect"};
        String tableName3 = "sections";
        String[] tableColumns4 = {"id", "email", "password", "salt", "numberLoginAttempts", "verificationToken",
                "emailVerified", "nickName", "firstName", "surname", "role"};
        String tableName4 = "users";

        System.out.println("the query for 'reports' is: "
                + getSaveEntitySqlByTableNameAndTableColumnNames(tableName1, tableColumns1));
        System.out.println("the query for 'conferences' is: "
                + getSaveEntitySqlByTableNameAndTableColumnNames(tableName2, tableColumns2));
        System.out.println("the query for 'sections' is: "
                + getSaveEntitySqlByTableNameAndTableColumnNames(tableName3, tableColumns3));
        System.out.println("the query for 'users' is: "
                + getSaveEntitySqlByTableNameAndTableColumnNames(tableName4, tableColumns4));


    }

    private static String getSaveEntitySqlByTableNameAndTableColumnNames(String tableName, String[] tableColumns) {
        StringBuilder saveEntitySql = new StringBuilder("insert into %s (");
        String[] tableColumnsToQuery = new String[tableColumns.length - 1];
        System.arraycopy(tableColumns, 1, tableColumnsToQuery, 0, tableColumns.length - 1);
        saveEntitySql.append(String.join(", ", tableColumnsToQuery));
        saveEntitySql.append(") values (");
        String[] secondPart = new String[tableColumns.length - 1];

        for (int i = 0; i < tableColumns.length - 1; i++) {
            secondPart[i] = "?";
        }
        saveEntitySql.append(String.join(",", secondPart));
        saveEntitySql.append(")");
        return String.format(String.valueOf(saveEntitySql), tableName);


    }
}