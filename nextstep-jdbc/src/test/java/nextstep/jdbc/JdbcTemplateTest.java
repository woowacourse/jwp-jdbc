package nextstep.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcTemplateTest {

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    void execute_query() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("questionId", 1);

        // when
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            List<Long> results = jdbcTemplate.executeQuery("SELECT * FROM questions WHERE questionId=:questionId",
                    params,
                    resultSet -> resultSet.getLong("questionId"));

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0)).isEqualTo(1);
        }

    }

    @Test
    void execute_update() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("userId", "ehem");
        params.put("password", "password");
        params.put("name", "에헴");
        params.put("email", "ehem@ehem.com");

        // when
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            int affected = jdbcTemplate.executeUpdate("INSERT INTO users VALUES(:userId, :password, :name, :email);", params);

            // then
            assertThat(affected).isEqualTo(1);
        }
    }

    @Test
    void invalid_query() {
        // when
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate()) {
            // then
            assertThrows(JdbcTemplateException.class,
                    () -> jdbcTemplate.executeUpdate("abcdefg", Collections.emptyMap()));
        }
    }

}