package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class ConnectionManager {
    private static final String DB_PROPERTIES = "db.properties";
    private static final String DB_DRIVER = "jdbc.driverClass";
    private static final String DB_URL = "jdbc.url";
    private static final String DB_USERNAME = "jdbc.username";
    private static final String DB_PW = "jdbc.password";
    private static final Properties PROPERTIES = new Properties();

    static {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (final InputStream stream = classLoader.getResourceAsStream(DB_PROPERTIES)) {
            PROPERTIES.load(Objects.requireNonNull(stream));
        } catch (final IOException | NullPointerException e) {
            throw new IllegalArgumentException("파일에서 설정을 읽을 수 없습니다.");
        }
    }

    public static DataSource getDataSource() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(PROPERTIES.getProperty(DB_DRIVER));
        ds.setUrl(PROPERTIES.getProperty(DB_URL));
        ds.setUsername(PROPERTIES.getProperty(DB_USERNAME));
        ds.setPassword(PROPERTIES.getProperty(DB_PW));
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
