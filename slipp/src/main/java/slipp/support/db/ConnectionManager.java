package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
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
        Properties properties = new Properties();
        BasicDataSource ds = new BasicDataSource();
        FileReader reader = null;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String driverClass = properties.getProperty("jdbc.driverClass");
        String url = properties.getProperty("jdbc.url");
        String userName = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        ds.setDriverClassName(driverClass);
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
