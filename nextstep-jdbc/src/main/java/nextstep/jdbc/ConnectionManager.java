package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {

    public static final String PROPERTIES_FILENAME = "db.properties";
    public static final String JDBC_DRIVER_CLASS = "jdbc.driverClass";
    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    public static DataSource getDataSource() throws IOException {
        Properties props = new Properties();
        props.load(ConnectionManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(props.getProperty(JDBC_DRIVER_CLASS));
        dataSource.setUrl(props.getProperty(JDBC_URL));
        dataSource.setUsername(props.getProperty(JDBC_USERNAME));
        dataSource.setPassword(props.getProperty(JDBC_PASSWORD));
        return dataSource;
    }
}
