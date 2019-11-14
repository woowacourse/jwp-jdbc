package slipp.support.db;

import org.apache.commons.dbcp2.BasicDataSource;
import slipp.support.db.exception.LoadPropertyFailException;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConnectionManager {

    private static final String DEFAULT_PROPS_PATH = "./slipp/src/main/resources/db.properties";

    public static DataSource getDataSource() {
        return getDataSource(DEFAULT_PROPS_PATH);
    }

    public static DataSource getDataSource(String propsPath) {
        BasicDataSource ds = new BasicDataSource();

        Properties props = loadProperties(propsPath);
        ds.setDriverClassName(props.getProperty("jdbc.driverClass"));
        ds.setUrl(props.getProperty("jdbc.url"));
        ds.setUsername("jdbc.username");
        ds.setPassword("jdbc.password");
        return ds;
    }

    private static Properties loadProperties(String propsPath) {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(propsPath);
            props.load(in);
            in.close();
            return props;
        } catch (IOException e) {
            throw new LoadPropertyFailException();
        }
    }
}
