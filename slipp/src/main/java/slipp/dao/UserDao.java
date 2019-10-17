package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>(ConnectionManager.getDataSource());

        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };

        jdbcTemplate.update(sql, preparedStatementSetter);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, email=?, name=? WHERE userId=?";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>(ConnectionManager.getDataSource());

        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getUserId());
        };

        jdbcTemplate.update(sql, preparedStatementSetter);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>(ConnectionManager.getDataSource());

        RowMapper<User> userRowMapper = rs ->
            new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>(ConnectionManager.getDataSource());

        PreparedStatementSetter preparedStatementSetter = pstmt -> pstmt.setString(1, userId);
        RowMapper<User> userRowMapper = rs ->
            new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));

        return jdbcTemplate.queryForObject(sql, preparedStatementSetter, userRowMapper);
    }
}
