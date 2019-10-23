package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasetTest {

    public static final String TABLE_NAME = "survey_results_public";
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource("src/test/java/sql/db.properties"));

    @Test
    @DisplayName("Coding As A Hobby")
    void findCodingAsHobby() throws Exception {
        Object o = jdbcTemplate.executeQueryWithUniqueResult("SELECT COUNT(*) FROM " + TABLE_NAME, rs -> rs.getObject("count(*)"))
                .orElseThrow(RuntimeException::new);

        assertThat((long) o).isEqualTo(98855L);
    }
}
