package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private String DB_DRIVER;
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PW;

    private ConnectionManager() {
        try {
            String resource = "D:\\woowaBros\\level_3\\jwp-jdbc\\slipp\\src\\main\\resources\\db.properties";
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(resource);
            properties.load(new java.io.BufferedInputStream(fis));

            DB_DRIVER = properties.getProperty("jdbc.driverClass");
            DB_URL = properties.getProperty("jdbc.url");
            DB_USERNAME = properties.getProperty("jdbc.username");
            DB_PW = properties.getProperty("jdbc.password");
        } catch (IOException e) {
            logger.error("()", e.getCause());
        }
    }

    public static ConnectionManager getInstance() {
        return ConnectionManagerHolder.instance;
    }

    public static DataSource getDataSource() {
        ConnectionManager connectionManager = getInstance();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(connectionManager.DB_DRIVER);
        ds.setUrl(connectionManager.DB_URL);
        ds.setUsername(connectionManager.DB_USERNAME);
        ds.setPassword(connectionManager.DB_PW);

        return ds;
    }

    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class ConnectionManagerHolder {
        static ConnectionManager instance = new ConnectionManager();
    }
}
