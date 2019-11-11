package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String LOCAL_DATABASE_PATH = "slipp/src/main/resources/db.properties";
    private static final String DRIVER_CLASS = "jdbc.driverClass";
    private static final String USER_NAME = "jdbc.username";
    private static final String URL = "jdbc.url";
    private static final String PASSWORD = "jdbc.password";

    public static DataSource getDataSource() {
        return getBasicDataSource(LOCAL_DATABASE_PATH);
    }

    public static DataSource getDataSource(String filePath) {
        return getBasicDataSource(filePath);
    }

    private static BasicDataSource getBasicDataSource(String path) {
        Properties properties = setProperties(path);

        String dbDriver = properties.getProperty(DRIVER_CLASS);
        String dbUrl = properties.getProperty(URL);
        String dbUserName = properties.getProperty(USER_NAME);
        String dbPassword = properties.getProperty(PASSWORD);

        return setBasicDataSource(dbDriver, dbUrl, dbUserName, dbPassword);
    }

    private static Properties setProperties(String path) {
        Properties properties = new Properties();
        try {
            Reader reader = new FileReader(path);
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            log.error("Connection 오류 : {}", e.getMessage());
            throw new IllegalArgumentException("properties 접근 불가능");
        }
    }

    private static BasicDataSource setBasicDataSource(String dbDriver, String dbUrl, String dbUserName, String dbPassword) {
        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(dbDriver);
        ds.setUrl(dbUrl);
        ds.setUsername(dbUserName);
        ds.setPassword(dbPassword);

        log.info("db connection info : {}, {}, {}, {}", dbDriver, dbUrl, dbUserName, dbPassword);

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
