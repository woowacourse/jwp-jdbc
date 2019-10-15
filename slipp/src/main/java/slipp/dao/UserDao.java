package slipp.dao;

import nextstep.jdbc.InsertJdbcTemplate;
import nextstep.jdbc.UpdateJdbcTemplate;
import slipp.domain.User;
import slipp.exception.NotFoundUserException;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        InsertJdbcTemplate insertJdbcTemplate = new InsertJdbcTemplate() {
            @Override
            public String createQueryForInsert() {
                return "INSERT INTO USERS VALUES (?, ?, ?, ?)";
            }

            @Override
            public void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };

        insertJdbcTemplate.insert(user);
    }

    public void update(User user) throws SQLException {
        UpdateJdbcTemplate updateJdbcTemplate = new UpdateJdbcTemplate() {
            @Override
            public String createQueryForUpdate() {
                return "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
            }

            @Override
            public void setValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };

        updateJdbcTemplate.update(user);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users";
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    String userId = rs.getString("userId");
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    users.add(new User(userId, password, name, email));
                }
                return users;
            }
        }
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                            rs.getString("email"));
                }
            }
            throw new NotFoundUserException();
        }
    }
}
