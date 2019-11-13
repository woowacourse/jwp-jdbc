package nextstep.jdbc;

import nextstep.jdbc.exception.PropertyLoadException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private static String driver;
    private static String url;
    private static String userName;
    private static String password;

    public static void initialize(String _driver, String _url, String _userName, String _password) {
        driver = _driver;
        url = _url;
        userName = _userName;
        password = _password;
    }

    public static void initialize(String path) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(path));
        } catch (IOException e) {
            log.debug("fail to load property {}", e.getMessage());
            throw new PropertyLoadException(e);
        }

        driver = properties.getProperty("jdbc.driverClass");
        url = properties.getProperty("jdbc.url");
        userName = properties.getProperty("jdbc.username");
        password = properties.getProperty("jdbc.password");
    }

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
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
