package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String DEFAULT_DB_DRIVER = "org.h2.Driver";
    private static final String DEFAULT_DB_URL = "jdbc:h2:mem:jwp-framework";
    private static final String DEFAULT_DB_USERNAME = "sa";
    private static final String DEFAULT_DB_PW = "";

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DEFAULT_DB_DRIVER);
        ds.setUrl(DEFAULT_DB_URL);
        ds.setUsername(DEFAULT_DB_USERNAME);
        ds.setPassword(DEFAULT_DB_PW);

        return ds;
    }

    public static DataSource getDataSource(String path) {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(path)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));

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
