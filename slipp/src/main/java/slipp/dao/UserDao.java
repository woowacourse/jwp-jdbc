package slipp.dao;

import slipp.domain.User;
import nextstep.jdbc.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao() {
        this.jdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(query, user.getUserId(),
                user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String query = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

        jdbcTemplate.update(query, user.getPassword(),
                user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String query = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.query(query, this::getUser);
    }

    public User findByUserId(String userId) {
        String query = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.queryForObject(query, this::getUser, userId);
    }

    private User getUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
    }
}
