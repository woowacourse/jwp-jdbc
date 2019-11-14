package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String DB_DRIVER = "jdbc.driverClass";
    private static final String DB_URL = "jdbc.url";
    private static final String DB_USERNAME = "jdbc.username";
    private static final String DB_PW = "jdbc.password";

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        Properties properties = new Properties();
        try {
            properties.load(ConnectionManager.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            log.error("err: {}", e.getMessage());
        }

        ds.setDriverClassName(properties.getProperty(DB_DRIVER));
        ds.setUrl(properties.getProperty(DB_URL));
        ds.setUsername(properties.getProperty(DB_USERNAME));
        ds.setPassword(properties.getProperty(DB_PW));
        return ds;
    }
}
