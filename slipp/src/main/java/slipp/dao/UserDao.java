package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    public UserDao() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        userRowMapper = resultSet -> new User(resultSet.getString("USERID"), resultSet.getString("PASSWORD")
                , resultSet.getString("NAME"), resultSet.getString("EMAIL"));
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password = ?, name = ?, " +
                "email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT userId, email, password, name FROM USERS";
        return jdbcTemplate.executeList(sql, userRowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return jdbcTemplate.execute(sql, userRowMapper, userId);
    }
}
