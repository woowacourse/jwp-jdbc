package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDao() {
        this.jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
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
                User.class);
    }

    public User findByUserId(String userId) {
        return jdbcTemplate.executeQuery("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                User.class, userId);
    }
}
