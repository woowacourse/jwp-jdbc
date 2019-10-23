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
    private static final String DEFAULT_FILE_PATH = "src/main/resources/db.properties";

    private ConnectionManager() {
    }

    public static DataSource getDataSource() {
        return getDataSource(DEFAULT_FILE_PATH);
    }

    public static DataSource getDataSource(String filePath) {
        Properties properties = readDBProperties(filePath);

        String driverClassName = properties.getProperty("jdbc.driverClass");
        String url = properties.getProperty("jdbc.url");
        String userName = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }

    private static Properties readDBProperties(String filePath) {
        try {
            Properties properties = new Properties();
            FileReader fileReader = new FileReader(filePath);
            properties.load(fileReader);
            return properties;
        } catch (IOException e) {
            log.debug("datasource creation failed : {}", e.getMessage());
            throw new DataSourceLookupFailureException(e.getMessage(), e);
        }
    }
}
