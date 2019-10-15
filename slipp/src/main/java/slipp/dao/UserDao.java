package slipp.dao;

import slipp.domain.User;

import java.util.List;

public class UserDao {
    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.save(sql, pstmtSetter);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET PASSWORD = ?, NAME = ?, EMAIL = ? WHERE USERID = ?";

        PreparedStatementSetter pstmtSetter = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.save(sql, pstmtSetter);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";

        RowMapper<User> rowMapper = rs -> {
            String id = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            return new User(id, password, name, email);
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        PreparedStatementSetter pstmtSetter = pstmt -> pstmt.setString(1, userId);

        RowMapper rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (User) jdbcTemplate.queryForObject(sql, rowMapper, pstmtSetter);
    }
}
