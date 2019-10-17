package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.jdbc.SqlExecuteStrategy;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private static final JdbcTemplate<User> JDBC_TEMPLATE = new JdbcTemplate<>(ConnectionManager.getDataSource());
    private static final RowMapper<User> ROW_MAPPER = resultSet -> {
        String userId = resultSet.getString("userId");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        return new User(userId, password, name, email);
    };

    public void insert(User user) {
        SqlExecuteStrategy sqlExecuteStrategy = preparedStatement -> {
            preparedStatement.setObject(1, user.getUserId());
            preparedStatement.setObject(2, user.getPassword());
            preparedStatement.setObject(3, user.getName());
            preparedStatement.setObject(4, user.getEmail());

            preparedStatement.executeUpdate();
        };
        JDBC_TEMPLATE.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", sqlExecuteStrategy);

    }

    public void update(User user) {
        SqlExecuteStrategy sqlExecuteStrategy = preparedStatement -> {
            preparedStatement.setObject(1, user.getPassword());
            preparedStatement.setObject(2, user.getName());
            preparedStatement.setObject(3, user.getEmail());
            preparedStatement.setObject(4, user.getUserId());

            preparedStatement.executeUpdate();
        };
        JDBC_TEMPLATE.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", sqlExecuteStrategy);
    }

    public List<User> findAll() {
        SqlExecuteStrategy sqlExecuteStrategy = PreparedStatement::executeQuery;
        return JDBC_TEMPLATE.readForList(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS", sqlExecuteStrategy);
    }

    public Optional<User> findByUserId(String id) {
        SqlExecuteStrategy sqlExecuteStrategy = preparedStatement -> {
            preparedStatement.setObject(1, id);

            preparedStatement.executeQuery();
        };
        return JDBC_TEMPLATE.readForObject(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS WHERE userId = ?", sqlExecuteStrategy);
    }
}
