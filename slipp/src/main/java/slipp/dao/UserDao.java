package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;

import java.util.List;

public class UserDao {
    public void insert(User user) {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();
        jdbcTemplate.update(query, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String query = "UPDATE users SET password=?, name=?, email=? WHERE userId=?";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();

        jdbcTemplate.update(query, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();

        return jdbcTemplate.select(
                query,
                resultSet -> new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));
    }

    public User findByUserId(String userId) {
        String query = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();

        return jdbcTemplate.selectForObject(
                query,
                resultSet -> new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")),
                userId);
    }
}
