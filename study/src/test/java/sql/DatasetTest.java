package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasetTest {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource("src/test/java/sql/db.properties"));

    @Test
    @DisplayName("Coding As A Hobby")
    void findCodingAsHobby_timeout() {
        String sql = "SELECT round((count(*) * 100 / (SELECT count(*) FROM survey_results_public)), 1) AS percent" +
                " FROM survey_results_public" +
                " WHERE hobby = 'Yes'";

        Object o = jdbcTemplate.executeQueryWithUniqueResult(sql, rs -> rs.getObject("percent"))
                .orElseThrow(RuntimeException::new);
        assertThat((BigDecimal) o).isEqualTo("80.8");
    }
}
