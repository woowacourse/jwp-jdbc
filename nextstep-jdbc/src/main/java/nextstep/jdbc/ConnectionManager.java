package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private static String DB_PROPERTIES = "db.properties";
    private DataSource dataSource;

    public ConnectionManager() {
        dataSource = makeDataSource();
    }

    Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    private DataSource makeDataSource() {
        Properties prop = new Properties();
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream(DB_PROPERTIES)) {
            prop.load(input);
        } catch (IOException e) {
            throw new DataAccessException();
        }
        return loadProperties(prop);
    }

    private DataSource loadProperties(Properties prop) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(prop.getProperty("jdbc.driver"));
        ds.setUrl(prop.getProperty("jdbc.url"));
        ds.setUsername(prop.getProperty("jdbc.username"));
        ds.setPassword(prop.getProperty("jdbc.password"));
        return ds;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
