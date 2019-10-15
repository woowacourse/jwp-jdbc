package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        jdbcTemplate.executeUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        List<User> users = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userId = rs.getString("userId");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");

                User user = new User(userId, password, name, email);
                users.add(user);
            }
        }
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            User user = executeQuery(pstmt);
            return user;
        }
    }

    private User executeQuery(final PreparedStatement pstmt) throws SQLException {
        User user = null;

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String retrievedUserId = rs.getString("userId");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");

                user = new User(retrievedUserId, password, name, email);
            }
        }
        return user;
    }
}
