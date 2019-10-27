package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.ResultSetHandler;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;
import java.util.Optional;

public class UserDao {
    private final ResultSetHandler<User> rsToUser = rs -> new User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
    );

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.queryForObjects(sql, rsToUser);
    }

    public Optional<User> findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, rsToUser, userId);
    }

    public void deleteByUserId(String userId) {
        String sql = "DELETE FROM USERS WHERE userId=?";
        jdbcTemplate.update(sql, userId);
    }

    public void deleteAll() {
        String sql = "DELETE FROM USERS";
        jdbcTemplate.update(sql);
    }
}
