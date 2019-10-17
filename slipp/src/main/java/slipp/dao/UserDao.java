package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

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
        JDBC_TEMPLATE.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        JDBC_TEMPLATE.update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?", user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return JDBC_TEMPLATE.readForList(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS");
    }

    public Optional<User> findByUserId(String id) {
        return JDBC_TEMPLATE.readForObject(ROW_MAPPER, "SELECT userId, password, name, email FROM USERS WHERE userId = ?", id);
    }
}
