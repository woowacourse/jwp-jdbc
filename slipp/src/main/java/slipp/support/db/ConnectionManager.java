package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String DEFAULT_FILe_PATH = "src/main/resources/db.properties";

    public static DataSource getDataSource() {
        return getDataSource(DEFAULT_FILe_PATH);
    }

    public static DataSource getDataSource(String filePath) {
        Properties properties = getDBPropertiesFrom(filePath);

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(properties.getProperty("jdbc.driverClass"));
        basicDataSource.setUrl(properties.getProperty("jdbc.url"));
        basicDataSource.setUsername(properties.getProperty("jdbc.username"));
        basicDataSource.setPassword(properties.getProperty("jdbc.password"));
        return basicDataSource;
    }

    private static Properties getDBPropertiesFrom(String filePath) {
        try {
            Properties properties = new Properties();
            FileReader fileReader = new FileReader(filePath);
            properties.load(fileReader);
            return properties;
        } catch(IOException e) {
            log.error("error while reading file path : {}", e.getMessage());
            throw new DataSourceLookupFailureException(e.getMessage(), e);
        }
    }
}
