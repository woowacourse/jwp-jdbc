package slipp.support.db;

import javax.sql.DataSource;

public class ConnectionManager {
    private static DataSource dataSource;

    public static DataSource getDataSource() {
        return dataSource;
    }
}