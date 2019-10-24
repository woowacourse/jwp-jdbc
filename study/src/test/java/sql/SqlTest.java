package sql;

import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.DataBasePropertyReader;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {
    private String path = "/Users/mac/level3/jwp-jdbc/slipp/src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SqlTest.class);
    @BeforeEach
    void setUp() {
        DataBasePropertyReader dataBasePropertyReader = new DataBasePropertyReader(path);
        dataBasePropertyReader.readDataBaseProperty();
        jdbcTemplate = new JdbcTemplate(
            ConnectionGenerator.getDataSource(dataBasePropertyReader.getDriver(), dataBasePropertyReader.getUrl(),
                dataBasePropertyReader.getUserName(), dataBasePropertyReader.getPassword()));
        logger.debug("{}",dataBasePropertyReader.getDriver());
    }

    @Test
    void hobby() {
        String sql = "SELECT HOBBY, (COUNT(HOBBY) / (SELECT COUNT(*) FROM survey_results_public)) * 100 'RATIO'" +
            "FROM survey_results_public " +
            "GROUP BY hobby;";
        List<SurveyResult> surveyResults = jdbcTemplate.listQuery(sql, rs -> new SurveyResult(rs.getString("Hobby")));
//        assertThat(surveyResults.get(0).getHobby()).isEqualTo("No");
        assertTimeout(Duration.ofMillis(100), () -> {jdbcTemplate.listQuery(sql, rs -> new SurveyResult(rs.getString("Hobby")));});
    }
}
