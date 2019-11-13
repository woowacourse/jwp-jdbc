package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Problem1 {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    @DisplayName("coding as a hobby")
    void essentialProblem() {
        String query = "SELECT hobby, count(1) / (SELECT COUNT(1) FROM survey_results_public) * 100 respondents " +
                "FROM survey_results_public " +
                "GROUP BY hobby";

        List<Result> results = jdbcTemplate.executeQuery(
                query, Collections.emptyMap(),
                rs -> new Result(rs.getString("hobby"), rs.getDouble("respondents")));

        for (Result result : results) {
            if (result.getHobby().equals("Yes")) {
                assertThat(result.getRespondents()).isBetween(80.0, 81.0);
            } else if (result.getHobby().equals("No")) {
                assertThat(result.getRespondents()).isBetween(10.0, 20.0);
            }
        }

    }
}
