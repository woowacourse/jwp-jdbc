package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.exception.PropertiesLoadFailException;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    public static final String DEFAULT_PROPERTIES = "src/main/resources/db.properties";

    public static DataSource getDataSource() {
        return getDataSource(DEFAULT_PROPERTIES);
    }

    public static DataSource getDataSource(String propertiesPath) {
        Properties props = loadProperties(propertiesPath);
        BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(props.getProperty("jdbc.driverClass"));
        ds.setUrl(props.getProperty("jdbc.url"));
        ds.setUsername(props.getProperty("jdbc.username"));
        ds.setPassword(props.getProperty("jdbc.password"));
        return ds;
    }

    private static Properties loadProperties(String propetiesPath) {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(propetiesPath);
            props.load(fis);
            return props;
        } catch (IOException e) {
            throw new PropertiesLoadFailException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
