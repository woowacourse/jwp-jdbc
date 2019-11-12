package sql;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueryTest {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        ConnectionManager.initialize();
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    @Test
    void initializeTest() {
        // do nothing
    }
}
