package nextstep.jdbc;

import nextstep.jdbc.support.DataBaseInitializer;
import nextstep.jdbc.support.SampleDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class NamedParameterJdbcTemplateTest {
    private DataSource dataSource = SampleDataSource.getDataSource();
    private NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    private PropertyRowMapper<User> rowMapper = PropertyRowMapper.from(User.class);

    NamedParameterJdbcTemplateTest() {
        DataBaseInitializer.init(dataSource);
    }

    @Test
    void update() {
        // given
        final String userId = "admin";
        final String name = "name2";
        final String password = "password2";

        final String updateQuery = "UPDATE users SET password = :password, name = :name WHERE userId = :userId";
        final String selectQuery = "SELECT * FROM users WHERE userId = :userId";

        final Map<String, Object> params = Map.of("userId", userId, "name", name, "password", password);

        // when
        jdbcTemplate.update(updateQuery, params);
        final User actual = jdbcTemplate.executeForObject(selectQuery, Map.of("userId", userId), rowMapper);

        // then
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPassword()).isEqualTo(password);
    }

    @Test
    void queryForObject() {
        // given
        final String sql = "SELECT * FROM users WHERE userId = :userId";
        final Map<String, Object> params = new HashMap<>();
        params.put("userId", "admin");

        // when
        final User actual = jdbcTemplate.executeForObject(sql, params, rowMapper);

        // then
        assertThat(actual.getUserId()).isEqualTo("admin");
    }

    @Test
    void queryForList_파라미터_없는경() {
        // given
        final String sql = "SELECT * FROM users";

        // when
        final List<User> actual = jdbcTemplate.executeForList(sql, rowMapper);

        // then
        assertThat(actual).isNotEmpty();
    }

}