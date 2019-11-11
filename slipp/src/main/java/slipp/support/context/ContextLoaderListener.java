package slipp.support.context;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.support.db.ConnectionManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
    private static final String DB_RESOURCE = "slipp/src/main/resources/db.properties";
    private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));

        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(DB_RESOURCE);

            properties.load(new BufferedInputStream(fileInputStream));
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
            ds.setUrl(properties.getProperty("jdbc.url"));
            ds.setUsername(properties.getProperty("jdbc.username"));
            ds.setPassword(properties.getProperty("jdbc.password"));
            Field field = ConnectionManager.class.getDeclaredField("dataSource");
            field.setAccessible(true);
            field.set(field, ds);
        } catch (IOException e) {
            logger.error("DB 설정파일을 찾을 수 없습니다.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("리플렉션 오류");
        }

        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        logger.info("Completed Load ServletContext!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
