package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.DataBaseManager;

import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate = DataBaseManager.getJdbcTemplate();

    public void insert(User user) {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String query = "UPDATE users SET password=?, name=?, email=? WHERE userId=?";

        jdbcTemplate.update(query, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";

        return jdbcTemplate.select(
                query,
                resultSet -> new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));
    }

    public User findByUserId(String userId) {
        String query = "SELECT userId, password, name, email FROM users WHERE userId=?";

        return jdbcTemplate.selectForObject(
                query,
                resultSet -> new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")),
                userId);
    }
}
