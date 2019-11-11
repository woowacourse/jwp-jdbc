package nextstep.jdbc.db;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Properties;

public class BasicDataSourceFactory {
    public static BasicDataSource getDataSource(String driver, String url, String userName, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }

    public static BasicDataSource getDataSource(Properties properties) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        return ds;
    }
}
