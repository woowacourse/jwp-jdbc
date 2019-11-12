package sql;

import nextstep.jdbc.DBTemplate;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.QueryFailedException;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.ConnectionManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class CodingAsAHobbyTest {
    private static final Logger logger = LoggerFactory.getLogger(CodingAsAHobbyTest.class);

    private static final int MAX_DURATION = 100;
    private static final double ANSWER = 80.8;

    private final DBTemplate template = new JdbcTemplate(ConnectionManager.getDataSource());

    @Test
    void run() {
        final RowMapper<Integer> f = rs -> rs.getInt(1);
        final String resultQuery =
                "SELECT COUNT(*) * 100.0 / (SELECT COUNT(*) FROM survey_results_public)" +
                "FROM survey_results_public WHERE Hobby = 'Yes';";
        final String buildIndexQuery = "CREATE INDEX for_fun ON survey_results_public (Hobby);";

        try {
            this.template.execute(buildIndexQuery);
        } catch (QueryFailedException e) {
            logger.error(e.getMessage());
        }
        this.template.read(f, resultQuery).ifPresent(r -> assertEquals(ANSWER, r, 1.0));
        assertTimeout(Duration.ofMillis(MAX_DURATION), () -> {
            this.template.read(f, resultQuery);
        });
    }
}