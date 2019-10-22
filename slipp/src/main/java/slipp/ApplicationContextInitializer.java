package slipp;

import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Properties;

public class ApplicationContextInitializer {
    private static final String DB_PROPERTIES_PATH = "./src/main/resources/db.properties";
    private static final String DRIVER_CLASS_KEY = "jdbc.driverClass";
    private static final String URL_KEY = "jdbc.url";
    private static final String USERNAME_KEY = "jdbc.username";
    private static final String PASSWORD_KEY = "jdbc.password";

    public static void registerBeans() {
        registerDataSource();
        registerJdbcTemplate();
    }

    private static void registerDataSource() {
        Properties dbProperties = PropertiesFactory.generate(DB_PROPERTIES_PATH);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dbProperties.getProperty(DRIVER_CLASS_KEY));
        dataSource.setUrl(dbProperties.getProperty(URL_KEY));
        dataSource.setUsername(dbProperties.getProperty(USERNAME_KEY));
        dataSource.setPassword(dbProperties.getProperty(PASSWORD_KEY));

        ApplicationContext.register(dataSource);
    }

    private static void registerJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ApplicationContext.getBean(BasicDataSource.class));

        ApplicationContext.register(jdbcTemplate);
    }
}
