package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.jdbc.SqlExecuteStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(ConnectionManager.getDataSource());
    private static final RowMapper<User> ROW_MAPPER = resultSet -> {
        String userId = resultSet.getString("userId");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        return new User(userId, password, name, email);
    };

    private static UserDaoImpl userDaoImpl;

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        if (userDaoImpl == null) {
            userDaoImpl = new UserDaoImpl();
        }
        return userDaoImpl;
    }

    @Override
    public void insert(User user) {
        SqlExecuteStrategy sqlExecuteStrategy = preparedStatement -> {
            preparedStatement.setObject(1, user.getUserId());
            preparedStatement.setObject(2, user.getPassword());
            preparedStatement.setObject(3, user.getName());
            preparedStatement.setObject(4, user.getEmail());

            preparedStatement.executeUpdate();
        };
        try {
            JDBC_TEMPLATE.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", sqlExecuteStrategy);
        } catch (SQLException e) {
            log.error("err: {}", e.getErrorCode());
        }

    }

    @Override
    public void update(User user) {
        SqlExecuteStrategy sqlExecuteStrategy = preparedStatement -> {
            preparedStatement.setObject(1, user.getPassword());
            preparedStatement.setObject(2, user.getName());
            preparedStatement.setObject(3, user.getEmail());
            preparedStatement.setObject(4, user.getUserId());

            preparedStatement.executeUpdate();
        };
        try {
            JDBC_TEMPLATE.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?", sqlExecuteStrategy);
        } catch (SQLException e) {
            log.error("err: {}", e.getErrorCode());
        }
    }

    @Override
    public List<User> findAll() {
        SqlExecuteStrategy sqlExecuteStrategy = PreparedStatement::executeQuery;
        try {
            return JDBC_TEMPLATE.readForList(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS", sqlExecuteStrategy);
        } catch (SQLException e) {
            log.error("err: {}", e.getErrorCode());
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<User> findByUserId(String id) {
        SqlExecuteStrategy sqlExecuteStrategy = preparedStatement -> {
            preparedStatement.setObject(1, id);

            preparedStatement.executeQuery();
        };
        try {
            return JDBC_TEMPLATE.readForObject(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS WHERE userId = ?", sqlExecuteStrategy);
        } catch (SQLException e) {
            log.error("err: {}", e.getErrorCode());
        }
        return Optional.empty();
    }
}
