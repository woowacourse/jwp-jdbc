package sql;

import nextstep.jdbc.mapper.RowMapper;
import nextstep.jdbc.template.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;
import util.TestSqlUtil;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class PerformanceTest {
    private static final String INIT_DB_SQL = "initdb.sql";
    private static final String AFTER_DB_SQL = "afterdb.sql";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        TestSqlUtil.query(INIT_DB_SQL);
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    public void hobby() {
        final String sql1 =
                "SELECT DISTINCT ROUND((count(hobby) * 100 / (SELECT COUNT(hobby) FROM survey_results_public)), 1) as ratio\n" +
                        "FROM survey_results_public\n" +
                        "WHERE hobby = 'Yes'";

        final String sql2 =
                "SELECT DISTINCT ROUND((count(hobby) * 100 / (SELECT COUNT(hobby) FROM survey_results_public)), 1) as ratio\n" +
                        "FROM survey_results_public\n" +
                        "WHERE hobby = 'No'";

        assertThat(jdbcTemplate
                .executeForObject(sql1, (RowMapper<Object>) resultSet -> resultSet.getString(1)))
                .isEqualTo("80.8");

        assertThat(jdbcTemplate
                .executeForObject(sql2, (RowMapper<Object>) resultSet -> resultSet.getString(1)))
                .isEqualTo("19.2");

        assertTimeout(Duration.ofMillis(150L), () -> {
            jdbcTemplate.executeForObject(sql1, resultSet -> null);
        });

        assertTimeout(Duration.ofMillis(150L), () -> {
            jdbcTemplate.executeForObject(sql2, resultSet -> null);
        });
    }

    @Test
    public void avgYearsCodingProfOfDevType() {
        final String sql = "SELECT DevType, ROUND(AVG(YearsCodingProf), 1) as avgYearsCodingProf\n" +
                "FROM new_table\n" +
                "WHERE YearsCodingProf != \"NA\" AND DevType = ?\n" +
                "GROUP BY DevType";

        assertThat(
                jdbcTemplate.executeForObject(
                        sql,
                        (RowMapper<Double>) resultSet -> resultSet.getDouble("avgYearsCodingProf"),
                        "Engineering manager")
        ).isEqualTo(10.2);

        assertTimeout(Duration.ofMillis(100L), () -> {
            jdbcTemplate.executeForObject(
                    sql,
                    resultSet -> resultSet.getDouble("avgYearsCodingProf"),
                    "Engineering manager");
        });
    }

    @AfterEach
    public void tearDown() {
        TestSqlUtil.query(AFTER_DB_SQL);
    }
}
