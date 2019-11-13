package sql;

import slipp.support.db.ConnectionManager;
import slipp.support.db.exception.PropertiesIOException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
    private static final String RESOURCE = "db.properties";

    private String DB_DRIVER;
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PW;

    DBConnection() {
        final InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream(RESOURCE);
        final Properties properties = new Properties();

        if (inputStream != null) {
            try {
                properties.load(inputStream);
                DB_DRIVER = properties.getProperty("jdbc.driverClass");
                DB_URL = properties.getProperty("jdbc.url");
                DB_USERNAME = properties.getProperty("jdbc.username");
                DB_PW = properties.getProperty("jdbc.password");
            } catch (IOException e) {
                throw new PropertiesIOException(e);
            }
        }
    }

    String getDbDriver() {
        return DB_DRIVER;
    }

    String getDbUrl() {
        return DB_URL;
    }

    String getDbUsername() {
        return DB_USERNAME;
    }

    String getDbPw() {
        return DB_PW;
    }
}
