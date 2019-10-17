package slipp.dao;

import nextstep.jdbc.DBConnection;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.util.List;

public class UserDao {
    private JdbcTemplate template;

    public UserDao(DBConnection dbConnection) {
        this.template = new JdbcTemplate(dbConnection);
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        Object[] values = {user.getUserId(), user.getPassword(), user.getName(), user.getEmail()};
        template.execute(sql, values);
    }

    public void update(User user) {
        String sql = "UPDATE users SET password = ?, name = ?, email = ? WHERE userId = ?";
        Object[] values = {user.getPassword(), user.getName(), user.getEmail(), user.getUserId()};
        template.execute(sql, values);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        RowMapper<User> rm = getUserRowMapper();

        return template.query(sql, rm);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        Object[] values = {userId};
        RowMapper<User> rm = getUserRowMapper();

        return template.queryForObject(sql, rm, values);
    }

    private RowMapper<User> getUserRowMapper() {
        return rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"), rs.getString("email"));
    }
}
