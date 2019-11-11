package util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.support.db.ConnectionManager;

public class TestSqlUtil {

    public static void query(String sqlFile) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource(sqlFile));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }
}
