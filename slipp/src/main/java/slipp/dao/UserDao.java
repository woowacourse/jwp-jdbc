package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        List<Object> parameters = new ArrayList<>();
        parameters.add(user.getUserId());
        parameters.add(user.getPassword());
        parameters.add(user.getName());
        parameters.add(user.getEmail());

        jdbcTemplate.executeQuery(sql, parameters);

    }

    public void update(User user) {
        String sql = "UPDATE USERS " +
                "SET password=?, name=?, email=? " +
                "WHERE userId=?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(user.getPassword());
        parameters.add(user.getName());
        parameters.add(user.getEmail());
        parameters.add(user.getUserId());
        jdbcTemplate.executeQuery(sql, parameters);

    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.executeQueryThatHasResultSet(sql, new ArrayList<>(),
                (resultSet) -> {
                    List<User> users = new ArrayList<User>();
                    while (resultSet.next()) {
                        User user = new User(resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                        users.add(user);
                    }
                    return users;
                });
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(userId);

        return jdbcTemplate.executeQueryThatHasResultSet(sql, parameters,
                resultSet -> {
                    User user = null;
                    if (resultSet.next()) {
                        user = new User(resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                    }
                    return user;
                });
    }

}
