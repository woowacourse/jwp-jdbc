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
    private ResultSet rs = null;
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        List<Object> parameters = new ArrayList<>();
        parameters.add(user.getUserId());
        parameters.add(user.getPassword());
        parameters.add(user.getName());
        parameters.add(user.getEmail());

        jdbcTemplate.executeQuery(sql, parameters);

    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS " +
                "SET password=?, name=?, email=? " +
                "WHERE userId=?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(user.getPassword());
        parameters.add(user.getName());
        parameters.add(user.getEmail());
        parameters.add(user.getUserId());
        jdbcTemplate.executeQuery(sql, parameters);

    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<User>();
        ;
        String sql = "SELECT userId, password, name, email FROM USERS";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            ResultSet rs = executeQueryThatHasResult(pstmt, new ArrayList<>());
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

            List<Object> parameters = new ArrayList<>();
            parameters.add(userId);

            rs = executeQueryThatHasResult(pstmt, parameters);
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

    private ResultSet executeQueryThatHasResult(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        int index = 1;
        for (Object parameter : parameters) {
            pstmt.setObject(index, parameter);
            index++;
        }

        return pstmt.executeQuery();
    }
}
