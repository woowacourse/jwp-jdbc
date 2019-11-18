package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String RESOURCE = "db.properties";
    private static final Properties PROPERTIES = new Properties();
    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();

        try {
            PROPERTIES.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ds.setDriverClassName(PROPERTIES.getProperty("jdbc.driverClass"));
        ds.setUrl(PROPERTIES.getProperty("jdbc.url"));
        ds.setUsername(PROPERTIES.getProperty("jdbc.username"));
        ds.setPassword(PROPERTIES.getProperty("jdbc.password"));

        return ds;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
