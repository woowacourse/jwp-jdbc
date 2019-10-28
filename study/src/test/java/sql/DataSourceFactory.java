package sql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class DataSourceFactory {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    public static DataSource getDataSource() {
        Properties properties = getProperties();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        ds.setUrl(properties.getProperty("jdbc.url"));
        ds.setUsername(properties.getProperty("jdbc.username"));
        ds.setPassword(properties.getProperty("jdbc.password"));
        return ds;
    }

    private static Properties getProperties() {
        String resource = "/db.properties";
        Properties properties = new Properties();

        try {
            properties.load(DevTypeYearsCodingProfTests.class.getResourceAsStream(resource));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return properties;

    }
}
