package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcUserDao implements UserDao {


    private static final Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);
    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(ConnectionManager.getDataSource());
    private static final RowMapper<User> ROW_MAPPER = resultSet -> {
        String userId = resultSet.getString("userId");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        return new User(userId, password, name, email);
    };

    private static class LazyHolder {
        private static final JdbcUserDao INSTANCE = new JdbcUserDao();
    }

    public static JdbcUserDao getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void insert(User user) {
        try {
            JDBC_TEMPLATE.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", preparedStatement -> {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getEmail());
            });
        } catch (SQLException e) {
            logger.error("errorCode: {}", e.getErrorCode());
            e.printStackTrace();
        }
    }

    public void update(User user) {
        try {
            JDBC_TEMPLATE.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?", preparedStatement -> {
                preparedStatement.setString(1, user.getPassword());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getUserId());
            });
        } catch (SQLException e) {
            logger.error("errorCode: {}", e.getErrorCode());
            e.printStackTrace();
        }
    }

    public Optional<List<User>> findAll() {
        try {
            return JDBC_TEMPLATE.readForList(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS");
        } catch (SQLException e) {
            logger.error("errorCode: {}", e.getErrorCode());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findByUserId(String id) {
        try {
            return JDBC_TEMPLATE.readForObject(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS WHERE userId = ?", preparedStatement ->
                    preparedStatement.setString(1, id));
        } catch (SQLException e) {
            logger.error("errorCode: {}", e.getErrorCode());
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
