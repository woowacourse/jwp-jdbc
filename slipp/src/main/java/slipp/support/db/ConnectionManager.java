package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:mem:jwp-framework";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PW = "";

    public static DataSource getDataSource() {
        return getDataSource(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW);
    }

    public static DataSource getDataSource(Properties properties) {
        return getDataSource(properties.getProperty("jdbc.driverClass"),
                properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.username"),
                properties.getProperty("jdbc.password")
        );
    }

    public static DataSource getDataSource(String driverClass, String url, String userName, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    public static Properties getProperties() {
        try {
            FileInputStream fileInputStream = new FileInputStream("../slipp/src/main/resources/db.properties");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            Properties properties = new Properties();
            properties.load(bufferedInputStream);

            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
