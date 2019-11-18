package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Predicate;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String PROPERTIES_FILE_NAME = "db.properties";
    private static final String PROPERTY_DRIVER = "jdbc.driverClass";
    private static final String PROPERTY_URL = "jdbc.url";
    private static final String PROPERTY_USERNAME = "jdbc.username";
    private static final String PROPERTY_PASSWORD = "jdbc.password";
    private static final int MIN_IDLE = 5;
    private static final int MAX_IDLE = 10;
    private static final int MAX_OPEN_PREPARED_STATEMENTS = 100;

    public static DataSource getDataSource() {
        Predicate<Integer> p = (integer) -> integer > 0;


        BasicDataSource dataSource = new BasicDataSource();
        Properties properties = loadProperties();

        dataSource.setDriverClassName(properties.getProperty(PROPERTY_DRIVER));
        dataSource.setUrl(properties.getProperty(PROPERTY_URL));
        dataSource.setUsername(properties.getProperty(PROPERTY_USERNAME));
        dataSource.setPassword(properties.getProperty(PROPERTY_PASSWORD));
        dataSource.setMinIdle(MIN_IDLE);
        dataSource.setMaxIdle(MAX_IDLE);
        dataSource.setMaxOpenPreparedStatements(MAX_OPEN_PREPARED_STATEMENTS);

        return dataSource;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        ClassLoader classLoader = ConnectionManager.class.getClassLoader();

        try (InputStream fileInputStream = classLoader.getResourceAsStream(PROPERTIES_FILE_NAME)) {
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            log.error("Property load 실패", e);
            throw new PropertyLoadFailedException();
        }
    }
}