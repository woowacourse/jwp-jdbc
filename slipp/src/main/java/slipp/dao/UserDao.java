package slipp.dao;

import com.google.common.collect.Maps;
import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserDao {

    public void insert(User user) {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection())) {
            Map<String, Object> params = createUserParams(user);
            jdbcTemplate.executeUpdate("INSERT INTO USERS VALUES (:userId, :password, :name, :email)", params);
        }
    }

    public int update(User user) {
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection())) {
            Map<String, Object> params = createUserParams(user);
            return jdbcTemplate.executeUpdate("UPDATE USERS SET PASSWORD = :password, NAME = :name, EMAIL = :email WHERE USERID = :userId", params);
        }
    }

    private Map<String, Object> createUserParams(User user) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", user.getUserId());
        params.put("password", user.getPassword());
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        return params;
    }

    public List<User> findAll() {
        List<User> users;
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection())) {
            users = jdbcTemplate.executeQuery("SELECT * FROM USERS", Collections.emptyMap(),
                    this::extractUser);
        }

        return users;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }

    public User findByUserId(String userId) {
        List<User> users;
        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection())) {
            users = jdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS WHERE userid=:userId",
                    Collections.singletonMap("userId", userId),
                    this::extractUser);
        }

        return users.get(0);
    }
}
