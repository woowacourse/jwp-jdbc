package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void create(User user) {
        this.jdbcTemplate.insert(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) {
        this.jdbcTemplate.update(
                "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        return this.jdbcTemplate.selectAll(
                this::mapRowToUser,
                "SELECT userId, password, name, email FROM USERS"
        );
    }

    public User findByUserId(String userId) {
        return this.jdbcTemplate.select(
                this::mapRowToUser,
                "SELECT userId, password, name, email FROM USERS WHERE userId=?",
                userId
        );
    }

    public void deleteByUserId(String userId) {
        this.jdbcTemplate.delete("DELETE FROM USERS WHERE userId=?", userId);
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userId"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}