package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import slipp.support.db.exception.ReadPropertiesFailedException;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    public static final String DB_PROPERTIES_FILE_NAME = "db.properties";

    public static DataSource getDataSource() {
        Properties properties = readProperties();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        return ds;
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        ClassLoader classLoader = ConnectionManager.class.getClassLoader();

        try (InputStream fileInputStream = classLoader.getResourceAsStream(DB_PROPERTIES_FILE_NAME)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadPropertiesFailedException();
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
