package sql;

import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.support.db.DataBasePropertyReader;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static slipp.dao.UserDao.DB_DRIVER;
import static slipp.dao.UserDao.DB_PW;
import static slipp.dao.UserDao.DB_URL;
import static slipp.dao.UserDao.DB_USERNAME;

public class SqlTest {
    private static final Logger logger = LoggerFactory.getLogger(SqlTest.class);
    private String path = "/Users/mac/level3/jwp-jdbc/slipp/src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataBaseInitialSetter dataBaseInitialSetter = new DataBaseInitialSetter();
        jdbcTemplate = dataBaseInitialSetter.getJdbcTemplate();
    }

    @Test
    void hobby() {
        String sql = "SELECT HOBBY, (COUNT(HOBBY) / (SELECT COUNT(*) FROM survey_results_public)) * 100 'RATIO'" +
            "FROM survey_results_public " +
            "GROUP BY hobby;";

        assertTimeout(Duration.ofMillis(1000), () -> {
            jdbcTemplate.listQuery(sql, rs -> new SurveyResult(rs.getString("Hobby")));
        });
    }

    @Test
    void develop() {
        String sql = "SELECT Devtype, ROUND(AVG(years),1) as result from sub_table " +
            "WHERE sub_table.Devtype != ('NA') and sub_table.years != ('NA') " +
            "GROUP BY Devtype " +
            "ORDER BY result DESC;";
        assertTimeout(Duration.ofMillis(1200), () -> {
            jdbcTemplate.listQuery(sql, rs -> new DevtypeResult(rs.getString("Devtype"), rs.getString("result")));
        });
    }
}
