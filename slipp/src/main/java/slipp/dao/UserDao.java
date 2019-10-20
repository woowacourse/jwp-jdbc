package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private JdbcTemplate<User> jdbcTemplate;

    public UserDao() {
        this.jdbcTemplate = new JdbcTemplate<>(ConnectionManager.getDataSource());
    }

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?, email=?, name=? WHERE userId=?";

        jdbcTemplate.update(sql, user.getPassword(), user.getEmail(), user.getName(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";

        RowMapper<User> userRowMapper = rs ->
                new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));

        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        PreparedStatementSetter preparedStatementSetter = pstmt -> pstmt.setString(1, userId);
        RowMapper<User> userRowMapper = rs ->
                new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));

        return jdbcTemplate.queryForObject(sql, preparedStatementSetter, userRowMapper)
                .orElseThrow(SQLException::new);
    }
}
