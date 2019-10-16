package slipp.dao;

import slipp.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }

            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                return null;
            }
        };

        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)");
    }

    public void update(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }

            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                return null;
            }
        };
        jdbcTemplate.update("UPDATE USERS SET (password, name, email) = (?,?,?) WHERE userId = ?");
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) {
            }

            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };

        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS").stream()
                .map(object -> (User) object)
                .collect(Collectors.toList())
                ;
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }

            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
            }
        };

        return (User) jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?");
    }
}
