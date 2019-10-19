package nextstep.jdbc;

import nextstep.jdbc.exception.DatabaseAccessException;
import nextstep.jdbc.support.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcTemplateTest {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp_test.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    }

    @Test
    void update_exception() throws SQLException {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        assertThrows(DatabaseAccessException.class, () -> {
            jdbcTemplate.update(sql, "password", "name", "email", "userId", "hello");
        });
    }
}