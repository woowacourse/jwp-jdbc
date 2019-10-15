package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.util.List;

public class UserDao {

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate.save(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET PASSWORD = ?, NAME = ?, EMAIL = ? WHERE USERID = ?";
        JdbcTemplate.save(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";

        RowMapper<User> rowMapper = rs -> {
            String id = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");
            return new User(id, password, name, email);
        };

        return JdbcTemplate.query(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"),
                rs.getString("name"), rs.getString("email"));

        return (User) JdbcTemplate.queryForObject(sql, rowMapper, userId);
    }
}
