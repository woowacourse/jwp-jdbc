package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String DRIVER_CLASS = "jdbc.driverClass";
    private static final String URL = "jdbc.url";
    private static final String USERNAME = "jdbc.username";
    private static final String PASSWORD = "jdbc.password";

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        Properties properties = load("../../../../resources/db.properties");

        ds.setDriverClassName(properties.getProperty(DRIVER_CLASS));
        ds.setUrl(properties.getProperty(URL));
        ds.setUsername(properties.getProperty(USERNAME));
        ds.setPassword(properties.getProperty(PASSWORD));
        return ds;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Properties load(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
