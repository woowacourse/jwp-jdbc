package nextstep.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcTemplateTest {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

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
        List<Long> results = jdbcTemplate.executeQuery("SELECT * FROM questions WHERE questionId=:questionId",
                params,
                resultSet -> resultSet.getLong("questionId"));

        // then
        assertThat(results).hasSize(1);
        assertThat(results).first().isEqualTo(1L);
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
    void execute_query_single_query1() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("questionId", 1);

        // when
        Optional<Long> result = jdbcTemplate.executeQueryForSingleObject("SELECT * FROM questions WHERE questionId=:questionId",
                params,
                resultSet -> resultSet.getLong("questionId"));

        // then
        assertThat(result).isEqualTo(Optional.of(1L));
    }

    @Test
    @DisplayName("쿼리 결과 size가 0일 때 Optional.empty() 반환하는지 테스트")
    void execute_query_single_query2() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("questionId", 10);

        // when
        Optional<Long> result = jdbcTemplate.executeQueryForSingleObject("SELECT * FROM questions WHERE questionId=:questionId",
                params,
                resultSet -> resultSet.getLong("questionId"));

        // then
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void invalid_query() {
        // then
        assertThrows(JdbcTemplateException.class,
                () -> jdbcTemplate.executeUpdate("abcdefg", Collections.emptyMap()));
    }

}