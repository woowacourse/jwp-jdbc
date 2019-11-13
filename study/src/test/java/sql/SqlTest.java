package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTest {
    @BeforeEach
    void setUp() {
        ConnectionManager.initialize("../slipp/src/main/resources/db.properties");
    }

    @DisplayName("hobby 비율 확인")
    @Test
    void selectRatio() {
        String query = "SELECT hobby, ROUND(100 * COUNT(*) / (SELECT COUNT(*) FROM survey_results_public),1) AS ratio " +
                "FROM survey_results_public " +
                "GROUP BY hobby";

        JdbcTemplate<HobbyResponse> jdbcTemplate = new JdbcTemplate<>();
        List<HobbyResponse> hobbyResponses = jdbcTemplate.select(query, resultSet -> new HobbyResponse(resultSet.getString("hobby"),
                resultSet.getString("ratio")));

        assertThat(hobbyResponses)
                .isEqualTo(Arrays.asList(
                        new HobbyResponse("No", "19.2"),
                        new HobbyResponse("Yes", "80.8")));
    }
}
