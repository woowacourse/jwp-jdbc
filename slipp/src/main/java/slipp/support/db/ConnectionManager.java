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
    private static final String DB_DRIVER_KEY = "jdbc.driverClass";
    private static final String DB_URL_KEY = "jdbc.url";
    private static final String DB_USERNAME_KEY = "jdbc.username";
    private static final String DB_PW_KEY = "jdbc.password";

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        Properties prop = new Properties();

        try {
            prop.load(ConnectionManager.class.getResourceAsStream(("/db.properties")));
            ds.setDriverClassName(prop.getProperty(DB_DRIVER_KEY));
            ds.setUrl(prop.getProperty(DB_URL_KEY));
            ds.setUsername(prop.getProperty(DB_USERNAME_KEY));
            ds.setPassword(prop.getProperty(DB_PW_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

