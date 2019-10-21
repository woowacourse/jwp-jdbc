package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private DBConnection dbConnection;

    public ConnectionManager(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(dbConnection.getDriver());
        ds.setUrl(dbConnection.getUrl());
        ds.setUsername(dbConnection.getUserName());
        ds.setPassword(dbConnection.getPassword());
        return ds;
    }

}
