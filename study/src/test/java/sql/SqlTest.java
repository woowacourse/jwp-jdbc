package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

class SqlTest {
    private static final String SELECT_CODING_IS_HOBBY_SQL = "SELECT ROUND(s1.yes / (s1.yes + s2.not_yes) * 100, 1) yes, ROUND(s2.not_yes / (s1.yes + s2.not_yes) * 100, 1) no\n"
            + "FROM (SELECT COUNT(*) yes FROM survey_results_public WHERE hobby='YES') s1\n"
            + "CROSS JOIN (SELECT COUNT(*) not_yes FROM survey_results_public WHERE hobby='NO') s2\n";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    void Coding_is_Hobby() throws Exception {
        Hobby hobby = jdbcTemplate.queryForCount(SELECT_CODING_IS_HOBBY_SQL, resultSet ->
                new Hobby(
                        resultSet.getDouble("yes"),
                        resultSet.getDouble("no")
                )
        ).orElseThrow(IllegalArgumentException::new);

        assertEquals(80.8, hobby.getYes());
        assertEquals(19.2, hobby.getNo());
    }

    @Test
    void Coding_is_Hobby_TimeOut() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> jdbcTemplate.queryForCount(SELECT_CODING_IS_HOBBY_SQL, resultSet ->
                new Hobby(
                        resultSet.getDouble("yes"),
                        resultSet.getDouble("no")
                )
        ).orElseThrow(IllegalArgumentException::new), "시간 초과입니다.");
    }
}
