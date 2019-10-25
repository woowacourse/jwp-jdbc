package dbtest;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class DBTest {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(new ConnectionManager("db.properties"));
    private static final Logger log = LoggerFactory.getLogger(DBTest.class);

    @Test
    void infoCodingAsHobby() {
        String sql = "SELECT hobby, count(hobby) / (SELECT count(hobby) FROM jwp_jdbc.survey_results_public) from jwp_jdbc.survey_results_public group by hobby;";
        assertTimeout(Duration.ofMillis(100), () ->
                jdbcTemplate.query(sql, rs -> "hobby : " + rs.getString(1) + ", rate : " + rs.getDouble(2))
        );
    }
}
