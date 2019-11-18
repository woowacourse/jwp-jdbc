package slipp.support.db;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.DataSourcePropertiesBinder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SlippConnectionManager implements ConnectionManager {
    private static final String DB_PROPERTIES = "db.properties";

    public static DataSource getDataSource() {
        return DataSourcePropertiesBinder.bind(DB_PROPERTIES);
    }

    @Override
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
