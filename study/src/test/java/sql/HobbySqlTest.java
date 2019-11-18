package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

class HobbySqlTest {
    private static final String SELECT_CODING_IS_HOBBY_SQL = "SELECT ROUND(s1.yes / (s1.yes + s2.not_yes) * 100, 1) yes, ROUND(s2.not_yes / (s1.yes + s2.not_yes) * 100, 1) no\n"
            + "FROM (SELECT COUNT(*) yes FROM survey_results_public WHERE hobby='YES') s1\n"
            + "CROSS JOIN (SELECT COUNT(*) not_yes FROM survey_results_public WHERE hobby='NO') s2\n";
    private static final String ADD_INDEX_SQL = "ALTER TABLE survey_results_public ADD INDEX idx_hobby (hobby)";
    private static final String DROP_INDEX_SQL = "ALTER TABLE survey_results_public DROP INDEX idx_hobby";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        jdbcTemplate.query(ADD_INDEX_SQL);
    }

    @Test
    @DisplayName("코딩은 취미다")
    void codingIsHobby() throws Exception {
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
    @DisplayName("코딩은 취미다 timeout")
    void codingIsHobbyTimeOut() throws Exception {
        assertTimeout(Duration.ofMillis(100), () -> jdbcTemplate.queryForCount(SELECT_CODING_IS_HOBBY_SQL, resultSet ->
                new Hobby(
                        resultSet.getDouble("yes"),
                        resultSet.getDouble("no")
                )
        ).orElseThrow(IllegalArgumentException::new), "시간 초과입니다.");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.query(DROP_INDEX_SQL);
    }
}
