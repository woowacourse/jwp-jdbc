package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.exception.UserNotFoundException;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {

    private static final int UNIQUE_VALUE_INDEX = 0;
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

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

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper<User> rowMapper = generateUserRowMapper();
        List<User> users = jdbcTemplate.executeQuery(sql, rowMapper, userId);

        if (users.isEmpty()) {
            log.debug("user not found : userId={}", userId);
            throw new UserNotFoundException();
        }
        return users.get(UNIQUE_VALUE_INDEX);
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
