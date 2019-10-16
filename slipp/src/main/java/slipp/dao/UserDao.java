package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.util.List;

public class UserDao {
    private final RowMapper<User> userMapper = resultSet -> new User(
            resultSet.getString("userId"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("email"));

    public void insert(final User user) {
        new JdbcTemplate().write(
                "INSERT INTO USERS(userId, password, name, email) VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void update(final User user) {
        new JdbcTemplate().write(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        final String sql = "SELECT userId, password, name, email FROM USERS";
        return new JdbcTemplate().findItems(sql, userMapper);
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        return new JdbcTemplate().findItem(sql, userMapper, userId);
    }
}
