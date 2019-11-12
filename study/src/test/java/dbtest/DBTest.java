package dbtest;

import com.google.common.collect.Maps;
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

        Maps.newConcurrentMap();

    }

    @Test
    void yearsOfProfessionalCodingExperience() {
        String sql = "SELECT d.name, ROUND(AVG(s.YearsCodingProf),1) as exp\n" +
                "FROM dev_type as d, survey_results_public as s\n" +
                "WHERE s.DevType LIKE CONCAT('%', d.name, '%') and s.YearsCodingProf != 'NA'\n" +
                "GROUP BY d.name\n" +
                "ORDER BY exp DESC;";

        assertTimeout(Duration.ofMillis(3000), () ->
                jdbcTemplate.query(sql, rs -> "Developer Type : " + rs.getString(1) + ", Years of Professional : " + rs.getDouble(2))
        );

        for (String result : jdbcTemplate.query(sql, rs -> rs.getString(1) + ": " + rs.getDouble(2))) {
            log.info(result);
        }
    }
}
