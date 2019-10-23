package slipp.dao;

import nextstep.jdbc.template.JdbcTemplate;
import slipp.domain.User;
import slipp.mapper.MapperRegistry;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private static final String INSERT_SQL = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
    private static final String SELECT_USERS_SQL = "SELECT userId, password, name, email FROM USERS";
    private static final String SELECT_USER_SQL = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
    private static final MapperRegistry mapperRegistry = new MapperRegistry();

    private UserDao() {
    }

    public static UserDao getInstance() {
        return UserDaoHolder.instance;
    }

    public void insert(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update(INSERT_SQL,
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update(UPDATE_SQL,
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        return jdbcTemplate.executeForList(SELECT_USERS_SQL,
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));

    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        return jdbcTemplate.executeForObject(SELECT_USER_SQL,
                resultSet -> new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")),
                userId);
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(ConnectionManager.getConnection());
    }

    private static class UserDaoHolder {
        static UserDao instance = new UserDao();
    }
}
