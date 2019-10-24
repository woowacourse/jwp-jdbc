package slipp.support.db;

import slipp.exception.DataBasePropertyReadFailException;

import java.io.FileInputStream;
import java.util.Properties;

public class DataBasePropertyReader {
    private final String resource;
    private String driver;
    private String url;
    private String userName;
    private String password;

    public DataBasePropertyReader(final String resource) {
        this.resource = resource;
    }

    public void readDataBaseProperty() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(resource)) {
            properties.load(fileInputStream);
            driver = properties.getProperty("jdbc.driver");
            url = properties.getProperty("jdbc.url");
            userName = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            if (driver != null) {
                Class.forName(driver);
            }
        } catch (Exception e) {
            throw new DataBasePropertyReadFailException(e.getMessage());
        }
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
