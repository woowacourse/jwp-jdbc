package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    private static class LazyHolder {
        private static final UserDao userDao = new UserDao();
    }

    public static UserDao getInstance() {
        return LazyHolder.userDao;

    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        jdbcTemplate.executeUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        RowMapper<User> rowMapper = makeRowMapper();
        return jdbcTemplate.queryForMultipleEntities(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        RowMapper<User> rowMapper = makeRowMapper();
        return jdbcTemplate.queryForSingleEntity(sql, rowMapper, userId);
    }

    private RowMapper<User> makeRowMapper() {
        return rs -> {
            String retrievedUserId = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");

            return new User(retrievedUserId, password, name, email);
        };
    }
}
