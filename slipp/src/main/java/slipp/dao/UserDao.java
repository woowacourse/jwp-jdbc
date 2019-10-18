package slipp.dao;

import nextstep.jdbc.DBConnection;
import nextstep.jdbc.JdbcProxyTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.util.List;

public class UserDao {
    private JdbcProxyTemplate proxyTemplate;

    public UserDao(DBConnection dbConnection) {
        proxyTemplate = new JdbcProxyTemplate(dbConnection);
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        Object[] values = {user.getUserId(), user.getPassword(), user.getName(), user.getEmail()};
        proxyTemplate.execute(sql, values);
    }

    public void update(User user) {
        String sql = "UPDATE users SET password = ?, name = ?, email = ? WHERE userId = ?";
        Object[] values = {user.getPassword(), user.getName(), user.getEmail(), user.getUserId()};
        proxyTemplate.execute(sql, values);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        RowMapper<User> rm = getUserRowMapper();

        return proxyTemplate.query(sql, rm);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        Object[] values = {userId};
        RowMapper<User> rm = getUserRowMapper();

        return proxyTemplate.queryForObject(sql, rm, values);
    }

    private RowMapper<User> getUserRowMapper() {
        return rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
    }
}
