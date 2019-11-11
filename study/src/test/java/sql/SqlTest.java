package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {
    private static final String propertiesPath = "src/test/resources/db.properties";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource(propertiesPath));
    }

    @Test
    @DisplayName("Coding as hobby인 개발자의 비율은 80.8%다.")
    void coddingAsHobby() {
        String query = "select " +
                "round(selected.yes / selected.total, 3) as 'yes', " +
                "round((selected.total-selected.yes) / selected.total, 3) as 'no' " +
                "from (select count(*) as yes, (select count(*) from survey_results_public ) as total " +
                "from survey_results_public " +
                "where Hobby = 'yes') as selected;";

        String result = jdbcTemplate.executeQueryForObject(query,
                rs -> rs.getString("yes"))
                .orElseThrow(RuntimeException::new);
        assertThat(result).isEqualTo("0.808");
    }

    @Test
    @DisplayName("Hobby 성능 테스트")
    void codingAsHobbyTime() {
        String query = "select " +
                "round(selected.yes / selected.total, 2) as 'yes', " +
                "round((selected.total-selected.yes) / selected.total, 2) as 'no' " +
                "from (select count(*) as yes, (select count(*) from survey_results_public ) as total " +
                "from survey_results_public " +
                "where Hobby = 'yes') as selected;";

        assertTimeout(ofMillis(100), () -> {
            jdbcTemplate.executeQueryForObject(query,
                    rs -> rs.getString("yes"));
        });
    }
}
