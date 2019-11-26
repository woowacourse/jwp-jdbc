package sql;

import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.support.db.DataBasePropertyReader;

public class DataBaseInitialSetter {
    private static final String path = "/Users/mac/level3/jwp-jdbc/slipp/src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;
    private DataBasePropertyReader dataBasePropertyReader;

    public DataBaseInitialSetter() {
        dataBasePropertyReader = new DataBasePropertyReader(path);
        dataBasePropertyReader.readDataBaseProperty();

        jdbcTemplate = new JdbcTemplate(
            ConnectionGenerator.getDataSource(dataBasePropertyReader.getDriver(), dataBasePropertyReader.getUrl(),
                dataBasePropertyReader.getUserName(), dataBasePropertyReader.getPassword()));
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
