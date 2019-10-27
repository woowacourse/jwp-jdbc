package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class sqlTest {

    @Test
    void Coding_is_Hobby() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

        String sql = "SELECT ROUND(s1.yes / (s1.yes + s2.not_yes) * 100, 1) yes, ROUND(s2.not_yes / (s1.yes + s2.not_yes) * 100, 1) no\n"
                + "FROM (SELECT COUNT(*) yes FROM survey_results_public WHERE hobby='YES') s1\n"
                + "JOIN (SELECT COUNT(*) not_yes FROM survey_results_public WHERE hobby='NO') s2\n";

        assertTimeout(Duration.ofMillis(100), () -> jdbcTemplate.queryForCount(sql, resultSet ->
                new Hobby(
                        resultSet.getDouble("yes"),
                        resultSet.getDouble("no")
                )
        ).orElseThrow(IllegalArgumentException::new), "시간 초과입니다.");
    }
}
