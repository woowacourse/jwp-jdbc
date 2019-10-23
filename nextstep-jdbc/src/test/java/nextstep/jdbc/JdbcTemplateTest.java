package nextstep.jdbc;

import nextstep.jdbc.support.DataBaseInitializer;
import nextstep.jdbc.support.SampleDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTest {
    private DataSource dataSource = SampleDataSource.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    JdbcTemplateTest() {
        DataBaseInitializer.init(dataSource);
    }

    @Test
    void update_가변인자로_입_테스트() {
        final String userId = "admin";
        final String updateQuery = "UPDATE users SET password = ?, name = ? WHERE userId = ?";
        final String selectQuery = "SELECT * FROM users WHERE userId = ?";

        jdbcTemplate.update(updateQuery, "password2", "name2", userId);

        final User actual = jdbcTemplate.executeForObject(selectQuery, List.of(userId),rs ->
                new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getInt("age"))).get();

        assertThat(actual.getPassword()).isEqualTo("password2");

        assertThat(actual.getName()).isEqualTo("name2");
    }
}