package nextstep.jdbc.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

public class JdbcTemplateTest {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
    }
//
//    @Test
//    public void test() {
//        jdbcTemplate.executeForObject();
//    }

}
