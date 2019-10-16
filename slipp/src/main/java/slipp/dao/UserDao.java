package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(final ResultSet resultSet) throws SQLException {
            return new User(
                    resultSet.getString("userId"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("email"));
        }
    };

    public void insert(final User user) {
        final JdbcTemplate template = new JdbcTemplate();
        template.write(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)",
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
    }

    public void update(final User user) {
        final JdbcTemplate template = new JdbcTemplate();
        template.write(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        return new JdbcTemplate().findItems("SELECT userId, password, name, email FROM USERS", userMapper);
    }

    public User findByUserId(final String userId) {
        return new JdbcTemplate().findItem("SELECT userId, password, name, email FROM USERS WHERE userid = ?", userMapper, userId);
    }
}
