package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String DB_DRIVER = "jdbc.driverClass";
    private static final String DB_URL = "jdbc.url";
    private static final String DB_USERNAME = "jdbc.username";
    private static final String DB_PW = "jdbc.password";
    private static final String DB_PROPERTIES = "/db.properties";

    public static DataSource getDataSource() {
        Properties properties = getProperties();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty(DB_DRIVER));
        ds.setUrl(properties.getProperty(DB_URL));
        ds.setUsername(properties.getProperty(DB_USERNAME));
        ds.setPassword(properties.getProperty(DB_PW));
        return ds;
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(ConnectionManager.class.getResourceAsStream(DB_PROPERTIES));
        } catch (IOException e) {
            logger.error("error: {}", e);
        }
        return properties;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
