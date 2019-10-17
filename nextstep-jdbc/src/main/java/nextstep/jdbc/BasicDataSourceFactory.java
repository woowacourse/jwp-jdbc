package nextstep.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;

public class BasicDataSourceFactory {
    public static BasicDataSource getDataSource(String driver, String url, String userName, String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }
}
