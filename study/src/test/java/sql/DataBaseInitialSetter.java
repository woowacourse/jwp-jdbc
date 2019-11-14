package sql;

import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.support.db.DataBasePropertyReader;

import static slipp.dao.UserDao.DB_DRIVER;
import static slipp.dao.UserDao.DB_PW;
import static slipp.dao.UserDao.DB_URL;
import static slipp.dao.UserDao.DB_USERNAME;

public class DataBaseInitialSetter {
    private static final String path = "/Users/mac/level3/jwp-jdbc/slipp/src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;
    private DataBasePropertyReader dataBasePropertyReader;

    public DataBaseInitialSetter() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        dataBasePropertyReader = new DataBasePropertyReader(path);
        dataBasePropertyReader.readDataBaseProperty();
        DatabasePopulatorUtils.execute(populator, ConnectionGenerator.getDataSource(dataBasePropertyReader.getDriver(), dataBasePropertyReader.getUrl(),
            dataBasePropertyReader.getUserName(), dataBasePropertyReader.getPassword()));
        
        jdbcTemplate = new JdbcTemplate(
            ConnectionGenerator.getDataSource(dataBasePropertyReader.getDriver(), dataBasePropertyReader.getUrl(),
                dataBasePropertyReader.getUserName(), dataBasePropertyReader.getPassword()));
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
