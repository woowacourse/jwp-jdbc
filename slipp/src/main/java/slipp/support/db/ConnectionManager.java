package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DRIVER_CLASS_NAME = "jdbc.driverClass";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USERNAME = "jdbc.username";
    private static final String JDBC_PASSWORD = "jdbc.password";
    
    public static DataSource getDataSource() {
        Properties properties = getProperties();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty(DRIVER_CLASS_NAME));
        ds.setUrl(properties.getProperty(JDBC_URL));
        ds.setUsername(properties.getProperty(JDBC_USERNAME));
        ds.setPassword(properties.getProperty(JDBC_PASSWORD));
        return ds;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            Resource resource = new ClassPathResource(DB_PROPERTIES);
            FileReader fileReader = new FileReader(resource.getFile());
            properties.load(fileReader);
        } catch (IOException e) {
            logger.error(e.getMessage());
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
