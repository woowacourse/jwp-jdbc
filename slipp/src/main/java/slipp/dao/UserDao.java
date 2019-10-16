package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.ResultSetMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private JdbcTemplate jdbcTemplate;
    private ResultSetMapper<User> resultSetMapper;

    private UserDao() {
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        this.resultSetMapper = new ResultSetMapper<>(User.class);
    }

    private static class UserDaoLazyHolder {
        private static final UserDao INSTANCE = new UserDao();
    }

    public static UserDao getInstance() {
        return UserDaoLazyHolder.INSTANCE;
    }

    public void insert(User user) {
        jdbcTemplate.execute("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.execute("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS",
                resultSetMapper::mapList);
    }

    public Optional<User> findByUserId(String userId) {
        User user = jdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS WHERE userId=?",
                resultSetMapper::mapObject, userId);
        return Optional.ofNullable(user);
    }
}
