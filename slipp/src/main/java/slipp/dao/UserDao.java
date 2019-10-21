package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.resultsetmapper.SimpleResultSetMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleResultSetMapper<User> simpleResultSetMapper;

    private UserDao() {
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        this.simpleResultSetMapper = new SimpleResultSetMapper<>(User.class);
    }

    private static class UserDaoLazyHolder {
        private static final UserDao INSTANCE = new UserDao();
    }

    public static UserDao getInstance() {
        return UserDaoLazyHolder.INSTANCE;
    }

    public void insert(User user) {
        jdbcTemplate.executeUpdate("INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.executeUpdate("UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.executeQueryForList("SELECT userId, password, name, email FROM USERS", simpleResultSetMapper);
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS WHERE userId=?",
                simpleResultSetMapper, userId);
    }
}
