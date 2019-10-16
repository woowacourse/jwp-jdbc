package nextstep.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateTest {
    private static final String selectAllQuery = "SELECT * FROM test_users";
    private static final String selectQuery = "SELECT * FROM test_users WHERE userId=?";
    private static final String insertQuery = "INSERT INTO test_users VALUES (?, ?, ?, ?)";
    private static final String updateQuery =
            "UPDATE test_users SET password=?, name=?, email=? WHERE userId=?";

    private JdbcTemplate<TestUser> jdbcTemplate;
    private RowMapper<TestUser> rowMapper = resultSet ->
            new TestUser(resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));

    @BeforeEach
    void setUp() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("test.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

        jdbcTemplate = new JdbcTemplate<>();
    }

    @Test
    void select() {
        List<TestUser> users = jdbcTemplate.select(selectAllQuery, rowMapper);
        TestUser user = users.get(0);

        assertThat(users.size()).isEqualTo(3);
        assertThat(user.getUserId()).isEqualTo("admin");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("히브리");
        assertThat(user.getEmail()).isEqualTo("히브리@woowa.com");
    }

    @Test
    void selectWithCondition() {
        String query = selectAllQuery + " WHERE name=?";

        List<TestUser> users = jdbcTemplate.select(query, rowMapper, "test");
        TestUser test1 = users.get(0);
        TestUser test2 = users.get(1);

        assertThat(users.size()).isEqualTo(2);
        assertThat(test1.getUserId()).isEqualTo("test1");
        assertThat(test2.getUserId()).isEqualTo("test2");
    }

    @Test
    void selectForObject() {
        TestUser user = jdbcTemplate.selectForObject(selectQuery, rowMapper, "admin");

        assertThat(user.getUserId()).isEqualTo("admin");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("히브리");
        assertThat(user.getEmail()).isEqualTo("히브리@woowa.com");
    }

    @Test
    void insert() {
        String id = "insertId";
        String password = "password";
        String name = "insert";
        String email = "insert@woowa.com";

        jdbcTemplate.update(insertQuery, id, password, name, email);

        TestUser user = jdbcTemplate.selectForObject(selectQuery, rowMapper, id);

        assertThat(user.getUserId()).isEqualTo(id);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void update() {
        String updatePassword = "update";
        String updateName = "update";
        String updateEmail = "update@woowa.com";

        jdbcTemplate.update(updateQuery, updatePassword, updateName, updateEmail, "admin");

        TestUser user = jdbcTemplate.selectForObject(selectQuery, rowMapper, "admin");

        assertThat(user.getUserId()).isEqualTo("admin");
        assertThat(user.getPassword()).isEqualTo(updatePassword);
        assertThat(user.getName()).isEqualTo(updateName);
        assertThat(user.getEmail()).isEqualTo(updateEmail);
    }
}