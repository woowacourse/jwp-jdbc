package sql;

import nextstep.jdbc.exception.FailConnectionException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DataSourceManager {
    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);

    public static DataSource getDataSource(String path) {
        Properties properties = new Properties();
        try (FileReader resources = new FileReader(path)) {
            properties.load(resources);

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
            ds.setUrl(properties.getProperty("jdbc.url"));
            ds.setUsername(properties.getProperty("jdbc.username"));
            ds.setPassword(properties.getProperty("jdbc.password"));
            return ds;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FailConnectionException(e);
        }
    }
}
