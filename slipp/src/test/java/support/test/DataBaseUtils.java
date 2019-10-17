package support.test;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.support.db.DataBaseManager;

public class DataBaseUtils {
    public static void initialize() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:jwp-framework");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);

        DataBaseManager.initialize(dataSource);
    }
}
