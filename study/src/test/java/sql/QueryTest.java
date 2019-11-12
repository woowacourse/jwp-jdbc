package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryTest {
    @BeforeEach
    void setUp() {
        ConnectionManager.initialize();
    }

    @Test
    void initializeTest() {
        // do nothing
    }
}
