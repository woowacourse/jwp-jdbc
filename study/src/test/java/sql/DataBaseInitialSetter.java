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

    public DataBaseInitialSetter() {
        jdbcTemplate = new JdbcTemplate(path);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
