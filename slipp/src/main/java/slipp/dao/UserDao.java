package slipp.dao;

import nextstep.jdbc.DBConnection;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    JdbcTemplate template;

    public UserDao(DBConnection dbConnection) {
        this.template = new JdbcTemplate(dbConnection);
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        Object[] values = {user.getUserId(), user.getPassword(), user.getName(), user.getEmail()};
        template.execute(sql, values);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET password = ?, name = ?, email = ? WHERE userId = ?";
        Object[] values = {user.getPassword(), user.getName(), user.getEmail(), user.getUserId()};
        template.execute(sql, values);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users";

        RowMapper<List<User>> rm = rs -> {
            List<User> users = new ArrayList<>();

            while (rs.next()) {
                User user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
                users.add(user);
            }

            return users;
        };

        return template.query(sql, rm);
    }


    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE userId = ?";
        Object[] values = {userId};

        RowMapper<User> rm = rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }

            return user;
        };

        return template.queryForObject(sql, rm, values);
    }
}
