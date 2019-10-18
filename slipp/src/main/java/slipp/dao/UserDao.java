package slipp.dao;

import nextstep.jdbc.DBTemplate;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static final RowMapper<User> mapRowToUser = rs -> new User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
    );

    private final DBTemplate template;

    public UserDao(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public void create(User user) {
        this.template.create(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail()
        );
    }

    public void update(User user) {
        this.template.update(
                "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        return this.template.readAll(
                this.mapRowToUser,
                "SELECT userId, password, name, email FROM USERS"
        );
    }

    public Optional<User> findByUserId(String userId) {
        return this.template.read(
                this.mapRowToUser,
                "SELECT userId, password, name, email FROM USERS WHERE userId=?",
                userId
        );
    }

    public void deleteByUserId(String userId) {
        this.template.delete("DELETE FROM USERS WHERE userId=?", userId);
    }
}