package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class ConnectionManager {
    private final String driver;
    private final String url;
    private final String userName;
    private final String password;
    private Connection connection;

    ConnectionManager(String driver, String url, String userName, String password) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.connection = initConnection();
    }

    private Connection initConnection() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    Connection getConnection() {
        return this.connection;
    }
}
