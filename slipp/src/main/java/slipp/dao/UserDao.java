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
import java.util.Optional;

public class UserDao implements Dao<User> {
    private JdbcTemplate jdbcTemplate;

    private UserDao() {
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
    }

    public static UserDao getInstance() {
        return LazyHolder.userDao;
    }

    @Override
    public void insert(User user) {
        Map<String, Object> params = createUserParams(user);
        jdbcTemplate.executeUpdate("INSERT INTO USERS VALUES (:userId, :password, :name, :email)", params);
    }

    @Override
    public int update(User user) {
        Map<String, Object> params = createUserParams(user);
        return jdbcTemplate.executeUpdate("UPDATE USERS SET PASSWORD=:password, NAME=:name, EMAIL=:email WHERE USERID=:userId", params);
    }

    private Map<String, Object> createUserParams(User user) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", user.getUserId());
        params.put("password", user.getPassword());
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        return params;
    }

    @Override
    public int delete(User user) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", user.getUserId());
        return jdbcTemplate.executeUpdate("DELETE FROM users WHERE userid = :userId", params);
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.executeQuery("SELECT * FROM USERS", Collections.emptyMap(), this::extractUser);

        return users;
    }

    @Override
    public Optional<User> findBy(String userId) {
        return jdbcTemplate.executeQueryForSingleObject("SELECT userId, password, name, email FROM USERS WHERE userid=:userId",
                Collections.singletonMap("userId", userId),
                this::extractUser);
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }

    private static class LazyHolder {
        private static final UserDao userDao = new UserDao();
    }
}
