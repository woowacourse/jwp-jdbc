package slipp.dao;

import slipp.domain.User;
import slipp.support.db.JdbcTemplate;
import slipp.support.db.SelectJdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDao {
    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };

        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)");
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };

        jdbcTemplate.update("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?");
    }

    public List<User> findAll() {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException { }

            @Override
            protected Object mapRow(ResultSet rs) {
                return null;
            }
        };

        return selectJdbcTemplate.query("SELECT userId, password, name, email FROM USERS")
                .stream()
                .map(object -> (User) object)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId) {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            protected void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }

            @Override
            protected Object mapRow(ResultSet rs) {
                return null;
            }
        };
        return (User) selectJdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?");
    }
}
