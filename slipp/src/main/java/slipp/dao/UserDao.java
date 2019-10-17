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
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getEmail());
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                return null;
            }
        };
        jdbcTemplate.insert(sql);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, user.getPassword());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getUserId());
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                return null;
            }
        };
        jdbcTemplate.insert(sql);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
                users.add(user);
            }
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
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            protected void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, userId);
            }

            @Override
            protected User mapRow(ResultSet rs) throws SQLException {
                User user = null;
                if (rs.next()) {
                    user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                }
                return user;
            }
        };
        return jdbcTemplate.objectQuery(sql , userId);
    }
}
