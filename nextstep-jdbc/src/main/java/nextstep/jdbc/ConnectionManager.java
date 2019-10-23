package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private Properties properties;
    private final DataSource ds;

    public ConnectionManager(String resource) {
        this.properties = loadProperties(resource);
        this.ds = getDataSource();
    }

    private Properties loadProperties(String resource) {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemClassLoader().getResource(resource).openStream());
        } catch (IOException e) {
            throw new RuntimeException("properties 파일을 읽을 수 없습니다.");
        }
        return properties;
    }

    public DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        return ds;
    }

    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
