package nextstep.jdbc.db;

import nextstep.jdbc.exception.DatabaseAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static DataSource dataSource;

    public static void init(DataSource customDataSource) {
        dataSource = customDataSource;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
