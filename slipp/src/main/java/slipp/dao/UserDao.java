package slipp.dao;

import nextstep.jdbc.mapper.Mapper;
import nextstep.jdbc.template.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private UserDao() {
    }

    public static UserDao getInstance() {
        return UserDaoHolder.instance;
    }

    public void insert(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        Mapper<List<User>> userMapper = resultSet -> {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));
            }
            return users;
        };

        return jdbcTemplate.execute("SELECT userId, password, name, email FROM USERS",
                userMapper);
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        Mapper<User> userMapper = resultSet -> {
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            }
            return new User();
        };

        return jdbcTemplate.execute("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                userMapper,
                userId);
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(ConnectionManager.getConnection());
    }

    private static class UserDaoHolder {
        static UserDao instance = new UserDao();
    }
}
