package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import slipp.support.db.exception.NotFoundProperties;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionManager {
    private static final BasicDataSource ds = new BasicDataSource();
    private static final Properties properties = new Properties();

    public static DataSource getDataSource() {
        initProperties();
        ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        return ds;
    }

    private static void initProperties() {
        try (InputStream inputStream = (ConnectionManager.class).getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new NotFoundProperties();
        }
    }
}
