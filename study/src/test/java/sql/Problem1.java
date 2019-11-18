package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class Problem1 {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    @DisplayName("coding as a hobby")
    void essentialProblem() {
        String query = "SELECT hobby, count(1) / (SELECT COUNT(1) FROM survey_results_public) * 100 respondents " +
                "FROM survey_results_public " +
                "GROUP BY hobby";

        List<Hobby> hobbies = jdbcTemplate.executeQuery(
                query, Collections.emptyMap(),
                rs -> new Hobby(rs.getString("hobby"), rs.getDouble("respondents")));

        assertThat(hobbies).hasSize(2);
        for (Hobby hobby : hobbies) {
            if (hobby.getHobby().equals("Yes")) {
                assertThat(hobby.getRespondents()).isBetween(80.0, 81.0);
            } else if (hobby.getHobby().equals("No")) {
                assertThat(hobby.getRespondents()).isBetween(10.0, 20.0);
            }
        }

    }

    @Test
    @DisplayName("coding as a hobby 100ms 내에 실행하기")
    void advancedProblem() {
        /** README.md 파일의 쿼리를 먼저 실행시켜 인덱스를 생성해주세요! **/

        String query = "SELECT hobby, count(1) / (SELECT COUNT(1) FROM survey_results_public) * 100 respondents " +
                "FROM survey_results_public " +
                "GROUP BY hobby";

        assertTimeout(Duration.ofMillis(100), () -> jdbcTemplate.executeQuery(
                query, Collections.emptyMap(),
                rs -> new Hobby(rs.getString("hobby"), rs.getDouble("respondents"))));

    }
}
