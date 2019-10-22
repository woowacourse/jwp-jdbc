package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private static final String DB_PROPERTIES_NAME = "db.properties";

    private static final String DB_DRIVER;
    private static final String DB_URL;
    private static final String DB_USERNAME;
    private static final String DB_PW;

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream(DB_PROPERTIES_NAME)) {
            if (inputStream == null) {
                throw new NullPointerException("InputStream is null");
            }
            props.load(inputStream);
            DB_DRIVER = props.getProperty("jdbc.driverClass");
            DB_URL = props.getProperty("jdbc.url");
            DB_USERNAME = props.getProperty("jdbc.username");
            DB_PW = props.getProperty("jdbc.password");
        } catch (Exception e) {
            log.error("Cannot Load Database Properties File : ", e);
            throw new PropertiesAccessException(e);
        }
    }

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);
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
