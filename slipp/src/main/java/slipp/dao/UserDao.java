package slipp.dao;

import slipp.domain.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        jdbcTemplate.save(sql);
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET PASSWORD = ?, NAME = ?, EMAIL = ? WHERE USERID = ?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };
        jdbcTemplate.save(sql);
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

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) {
            }
        };
        return jdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email"));

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };
        return (User) jdbcTemplate.queryForObject(sql, rowMapper);
    }
}
