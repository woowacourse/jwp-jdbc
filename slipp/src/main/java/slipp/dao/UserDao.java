package slipp.dao;

import nextstep.jdbc.InsertJdbcTemplate;
import nextstep.jdbc.UpdateJdbcTemplate;
import slipp.dao.exception.SetValueException;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        InsertJdbcTemplate insertJdbcTemplate = new InsertJdbcTemplate();
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        insertJdbcTemplate.insert(sql, user);
    }

    public void update(User user) throws SQLException {
        UpdateJdbcTemplate updateJdbcTemplate = new UpdateJdbcTemplate();
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        updateJdbcTemplate.update(sql, user);
    }

    public List<User> findAll() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        List<User> users = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT * FROM USERS";
            pstmt = con.prepareStatement(sql);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                users.add(new User(resultSet.getString("userId"), resultSet.getString("password"), resultSet.getString("name"), resultSet.getString("email")));
            }
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }

        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
            pstmt = con.prepareStatement(sql);
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
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
