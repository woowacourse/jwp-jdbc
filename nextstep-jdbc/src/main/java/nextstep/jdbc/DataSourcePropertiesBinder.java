package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourcePropertiesBinder {
    private static final String PREFIX = "jdbc.";
    private static final Properties PROPERTIES = new Properties();

    public static DataSource bind(String resource) {
        try (InputStream inputStream
                     = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            PROPERTIES.load(inputStream);

            return createDataProperties();
        } catch (IOException e) {
            throw new DataPropertiesBinderException(e, e.getMessage());
        }
    }

    private static DataSource createDataProperties() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(PROPERTIES.getProperty(key("driverClass")));
        dataSource.setUrl(PROPERTIES.getProperty(key("url")));
        dataSource.setUsername(PROPERTIES.getProperty(key("username")));
        dataSource.setPassword(PROPERTIES.getProperty(key("password")));

        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            throw new ConnectionFailException(e);
        }

        return dataSource;
    }

    private static String key(String key) {
        return PREFIX + key;
    }
}
