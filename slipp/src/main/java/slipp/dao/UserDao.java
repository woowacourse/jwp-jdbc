package slipp.dao;

import slipp.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            @Override
            Object mapRow(ResultSet resultSet) throws SQLException {
                return null;
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

            @Override
            Object mapRow(ResultSet resultSet) throws SQLException {
                return null;
            }
        };
        jdbcTemplate.save(sql);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            Object mapRow(ResultSet resultSet) throws SQLException {
                String id = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                return new User(id, password, name, email);
            }

            @Override
            void setValues(PreparedStatement pstmt) {
            }
        };
        return jdbcTemplate.query(sql);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            Object mapRow(ResultSet resultSet) throws SQLException {
                return new User(resultSet.getString("userId"), resultSet.getString("password"),
                        resultSet.getString("name"), resultSet.getString("email"));
            }

            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };
        return (User) jdbcTemplate.queryForObject(sql);
    }
}
