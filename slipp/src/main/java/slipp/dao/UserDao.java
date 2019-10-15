package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao() {
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final List<Object> params = List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        jdbcTemplate.update(params, sql);
    }

    public void update(User user) {
        final String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?";
        final List<Object> params = List.of(user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
        jdbcTemplate.update(params, sql);
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM USERS";
        return jdbcTemplate.executeQuery(sql, rs -> {
            final List<User> result = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
                result.add(user);
            }
            return result;
        });
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        final List<Object> params = List.of(userId);

        return jdbcTemplate.executeQuery(params, sql, rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;
        });
    }

    public void deleteByUserId(final String userId) {
        final String sql = "DELETE FROM USERS WHERE userId = ?";
        final List<Object> params = List.of(userId);
        jdbcTemplate.update(params, sql);
    }
}
