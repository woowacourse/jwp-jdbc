package nextstep.jdbc;

import nextstep.jdbc.utils.DataSourceUtils;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceFactory {
    public static final String DB_DRIVER = "driverClass";
    public static final String DB_URL = "url";
    public static final String DB_USERNAME = "username";
    public static final String DB_PASSWORD = "password";

    private final DataSource dataSource;

    public DataSourceFactory(final String filePath) {
        final Properties properties = DataSourceUtils.createProperties(filePath);
        this.dataSource = createDataSource(properties);
    }

    public DataSource createDataSource(final Properties properties) {
        final BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName(properties.getProperty(DB_DRIVER));
        basicDataSource.setUrl(properties.getProperty(DB_URL));
        basicDataSource.setUsername(properties.getProperty(DB_USERNAME));
        basicDataSource.setPassword(properties.getProperty(DB_PASSWORD));
        return basicDataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
