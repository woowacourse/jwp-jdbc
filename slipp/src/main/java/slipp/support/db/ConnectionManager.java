package slipp.support.db;

import nextstep.jdbc.DataAccessException;
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
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:jwp-framework";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    public static DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(DB_DRIVER);
        ds.setUrl(DB_URL);
        ds.setUsername(DB_USERNAME);
        ds.setPassword(DB_PW);
        return ds;
    }

    public static DataSource getDataSource(String path) {
        try {
            Properties properties = new Properties();
            BasicDataSource ds = new BasicDataSource();
            FileReader reader = new FileReader(path);
            properties.load(reader);
            String driverClass = properties.getProperty("jdbc.driverClass");
            String url = properties.getProperty("jdbc.url");
            String userName = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");

            ds.setDriverClassName(driverClass);
            ds.setUrl(url);
            ds.setUsername(userName);
            ds.setPassword(password);

            return ds;
        } catch (IOException e) {
            log.error("연결실패", e);
            throw new DataAccessException("연결실패");
        }

    }

    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
