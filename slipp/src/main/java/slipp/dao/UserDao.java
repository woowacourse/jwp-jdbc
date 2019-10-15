package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        jdbcTemplate.updateQuery(con -> {
            String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            return pstmt;
        });
    }

    public void update(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        jdbcTemplate.updateQuery(con -> {
            String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());

            return pstmt;
        });
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());

        return jdbcTemplate.executeQuery(
                con -> {
                    String sql = "SELECT userId, password, name, email FROM USERS";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    return pstmt;
                },
                resultSet -> {
                    List<User> users = new ArrayList<>();
                    while (resultSet.next()) {
                        User user = new User(
                                resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                        users.add(user);
                    }

                    return users;
                });
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        return jdbcTemplate.executeQuery(
                con -> {
                    String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.setString(1, userId);
                    return pstmt;
                },
                resultSet -> {
                    User user = null;
                    if (resultSet.next()) {
                        user = new User(
                                resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                    }

                    return user;
                });
    }
}
