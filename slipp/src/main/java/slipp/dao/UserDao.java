package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.ApplicationContext;
import slipp.domain.User;

import java.util.List;

public class UserDao {
    private static final RowMapper<User> userRowMapper =
            resultSet -> new User(resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));

    private JdbcTemplate jdbcTemplate = ApplicationContext.getBean(JdbcTemplate.class);

    public void insert(User user) {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(query, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String query = "UPDATE users SET password=?, name=?, email=? WHERE userId=?";

        jdbcTemplate.update(query, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";

        return jdbcTemplate.select(query, userRowMapper);
    }

    public User findByUserId(String userId) {
        String query = "SELECT userId, password, name, email FROM users WHERE userId=?";

        return jdbcTemplate.selectForObject(query, userRowMapper, userId);
    }
}
