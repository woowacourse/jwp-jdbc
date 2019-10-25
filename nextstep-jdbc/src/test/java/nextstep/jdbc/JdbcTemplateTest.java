package nextstep.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcTemplateTest {

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
    }

    @AfterEach
    public void cleanup() {
        jdbcTemplate.close();
    }

    @Test
    void execute_query() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("questionId", 1);

        // when
        List<Long> results = jdbcTemplate.executeQuery("SELECT * FROM questions WHERE questionId=:questionId",
                params,
                resultSet -> resultSet.getLong("questionId"));

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(1);
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
        int affected = jdbcTemplate.executeUpdate("INSERT INTO users VALUES(:userId, :password, :name, :email);", params);

        // then
        assertThat(affected).isEqualTo(1);
    }

    @Test
    void invalid_query() {
        // when
        // then
        assertThrows(JdbcTemplateException.class,
                () -> jdbcTemplate.executeUpdate("abcdefg", Collections.emptyMap()));
    }

    // 배치 방식으로 실행되는지까지는 확인하기 어렵기 때문에
    // 갱신이 정상적으로 이루어지는지를 검증합니다.
    @Test
    void bulk_update() {
        // given
        String query = "INSERT INTO users VALUES(:userId, :password, :name, :email);";
        Map<String, Object> params1 = new HashMap<>();
        params1.put("userId", "ehem");
        params1.put("password", "password");
        params1.put("name", "에헴");
        params1.put("email", "ehem@ehem.com");

        Map<String, Object> params2 = new HashMap<>();
        params2.put("userId", "ethan");
        params2.put("password", "password");
        params2.put("name", "에단");
        params2.put("email", "ehtan@ethan.com");

        // when
        int affected = jdbcTemplate.executeBulkUpdate(query, Arrays.asList(params1, params2));

        // then
        assertThat(affected).isEqualTo(2);
    }
}