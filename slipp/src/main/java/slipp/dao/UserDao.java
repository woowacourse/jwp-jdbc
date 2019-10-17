package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.dao.exception.UserNotFoundException;
import slipp.domain.User;

import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

        jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";

        return jdbcTemplate.query(sql, createRowMapper());
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        return jdbcTemplate.query(sql, createRowMapper(), preparedStatement ->
            preparedStatement.setString(1, userId)
        ).orElseThrow(UserNotFoundException::new);
    }

    private RowMapper<User> createRowMapper() {
        return (resultSet) ->
                new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
    }
}
