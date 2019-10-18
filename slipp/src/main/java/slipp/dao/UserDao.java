package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) throws SQLException {
        String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?";
        jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";

        return jdbcTemplate.queryForObject(sql,
                pstmt -> {},
                this::getUsers);
    }

    private List<User> getUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email")));
        }
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.queryForObject(sql,
                pstmt -> pstmt.setString(1, userId),
                this::getUser);
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
        }
        return user;
    }
}
