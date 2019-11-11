package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {
    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);

    private String driver;
    private String url;
    private String userName;
    private String password;

    public DBConnection(String driver, String url, String userName, String password) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public DBConnection() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);

            driver = properties.getProperty("jdbc.driverClass");
            url = properties.getProperty("jdbc.url");
            userName = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
        } catch (IOException e) {
            log.debug(e.getMessage());
            throw new PropertiesLoadException(e);
        }
    }

    public static DBConnection getH2Connection(String driver, String url, String userName, String password) {
        return new DBConnection(driver, url, userName, password);
    }

    public static DBConnection getMysqlConnection() {
        return new DBConnection();
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
