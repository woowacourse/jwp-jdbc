package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.exception.UserNotFoundException;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    private UserDao() {
    }

    private static class LazyHolder {
        static final UserDao INSTANCE = new UserDao();
    }

    public static UserDao getInstance() {
        return LazyHolder.INSTANCE;
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

        RowMapper<User> rowMapper = generateUserRowMapper();
        return jdbcTemplate.executeQuery(sql, rowMapper);
    }

    public User findUserById(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        RowMapper<User> rowMapper = generateUserRowMapper();
        return jdbcTemplate.executeQueryWithUniqueResult(sql, rowMapper, userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private RowMapper<User> generateUserRowMapper() {
        return rs -> {
            String retrievedUserId = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");

            return new User(retrievedUserId, password, name, email);
        };
    }
}
