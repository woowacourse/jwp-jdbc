package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String DB_PROPERTIES_PATH = "/db.properties";
    private static final String DB_DRIVER = "jdbc.driverClass";
    private static final String DB_URL = "jdbc.url";
    private static final String DB_USERNAME = "jdbc.username";
    private static final String DB_PW = "jdbc.password";
    private static final String DB_DRIVER_MANUAL = "org.h2.Driver";
    private static final String DB_URL_MANUAL = "jdbc:h2:mem:jwp-framework";
    private static final String DB_USERNAME_MANUAL = "sa";
    private static final String DB_PW_MANUAL = "";

    public static DataSource getDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        try {
            final Properties properties = new Properties();
            properties.load(ConnectionManager.class.getResourceAsStream(DB_PROPERTIES_PATH));
            ds.setDriverClassName(properties.getProperty(DB_DRIVER));
            ds.setUrl(properties.getProperty(DB_URL));
            ds.setUsername(properties.getProperty(DB_USERNAME));
            ds.setPassword(properties.getProperty(DB_PW));
        } catch (IOException e) {
            ds.setDriverClassName(DB_DRIVER_MANUAL);
            ds.setUrl(DB_URL_MANUAL);
            ds.setUsername(DB_USERNAME_MANUAL);
            ds.setPassword(DB_PW_MANUAL);
        }
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