package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.jdbc.SqlExecuteStrategy;
import slipp.dao.exception.UserNotFoundException;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.execute(sql, createSqlExecuteStrategy(user));
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

        jdbcTemplate.execute(sql, createSqlExecuteStrategy(user));
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";

        return jdbcTemplate.execute(sql, createRowMapper());
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=?";

        return jdbcTemplate.queryForObject(sql, createRowMapper(), preparedStatement ->
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

    private SqlExecuteStrategy createSqlExecuteStrategy(User user) {
        return preparedStatement -> {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());
        };
    }
}
