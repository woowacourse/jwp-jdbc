package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao() {
        this.jdbcTemplate = JdbcTemplate.getInstance();
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final List<String> params = List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        jdbcTemplate.update(params, sql);
    }

    public void update(User user) {
        final String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?";
        final List<String> params = List.of(user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
        jdbcTemplate.update(params, sql);

    }

    public List<User> findAll() throws SQLException {
        final String sql = "SELECT * FROM USERS";
        final RowMapper<List<User>> rowMapper = rs -> {
            final List<User> result = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"));
                result.add(user);
            }
            return result;
        };
        return jdbcTemplate.executeQuery(Collections.EMPTY_LIST, sql, rowMapper);
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
