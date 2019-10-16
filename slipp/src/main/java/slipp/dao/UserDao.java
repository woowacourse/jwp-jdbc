package slipp.dao;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    ResultSet rs = null;


    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
        }
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS " +
                "SET password=?, name=?, email=? " +
                "WHERE userId=?";
        // TODO 구현 필요함.
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());

            pstmt.executeUpdate();
        }
    }

    public List<User> findAll() throws SQLException {
        // TODO 구현 필요함.
        List<User> users = new ArrayList<User>();
        ;
        String sql = "SELECT userId, password, name, email FROM USERS";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            User user;
            while (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                users.add(user);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            return user;

        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
}
