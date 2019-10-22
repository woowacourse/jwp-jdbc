package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.SimpleRowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleRowMapper<User> simpleRowMapper;

    private UserDao() {
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        this.simpleRowMapper = new SimpleRowMapper<>(User.class);
    }

    private static class UserDaoLazyHolder {
        private static final UserDao INSTANCE = new UserDao();
    }

    public static UserDao getInstance() {
        return UserDaoLazyHolder.INSTANCE;
    }

    public void insert(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.update("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.queryForList("SELECT userId, password, name, email FROM USERS", simpleRowMapper);
    }

    public Optional<User> findByUserId(String userId) {
        User user = jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userId=?",
                simpleRowMapper, userId);
        return Optional.ofNullable(user);
    }
}
