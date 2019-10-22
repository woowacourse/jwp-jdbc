package nextstep.jdbc;



import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionGenerator {
    public static DataSource getDataSource(String driverName, String url, String userName, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverName);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }

    public static Connection getConnection(String driverName, String url, String userName, String password) {
        try {
            return getDataSource(driverName, url, userName, password).getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
