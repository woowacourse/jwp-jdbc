package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.dao.exception.UserNotFoundException;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public static UserDao getInstance() {
        return LazyHolder.USER_DAO;
    }

    private static class LazyHolder {
        private static final UserDao USER_DAO = new UserDao();
    }

    private UserDao() {
        DataSource datasource = ConnectionManager.getDataSource();
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.executeQuery(sql, this::extractUser);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";
        return jdbcTemplate.executeQueryForObject(sql, this::extractUser, userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email"));
    }
}
