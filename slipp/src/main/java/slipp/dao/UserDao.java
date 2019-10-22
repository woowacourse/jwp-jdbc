package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(ConnectionManager.getDataSource());
    private static final RowMapper<User> ROW_MAPPER = resultSet -> {
        String userId = resultSet.getString("userId");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        return new User(userId, password, name, email);
    };

    public void insert(User user) {
        JDBC_TEMPLATE.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", preparedStatement -> {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        JDBC_TEMPLATE.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?", preparedStatement -> {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        return JDBC_TEMPLATE.readForList(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS");
    }

    public User findByUserId(String id) {
        return JDBC_TEMPLATE.readForObject(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS WHERE userId = ?", preparedStatement ->
                preparedStatement.setString(1, id));
    }
}
