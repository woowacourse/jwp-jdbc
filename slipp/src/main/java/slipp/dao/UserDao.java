package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.executeQuery(sql, (Object... objects) -> {
            List<Object> parameters = new ArrayList<>(Arrays.asList(objects));
            return parameters;
        }, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

    }

    public void update(User user) {
        String sql = "UPDATE USERS " +
                "SET password=?, name=?, email=? " +
                "WHERE userId=?";

        jdbcTemplate.executeQuery(sql, (Object... objects) -> {
            List<Object> parameters = new ArrayList<>(Arrays.asList(objects));
            return parameters;
        }, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.executeQueryThatHasResultSet(sql,
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
                },
                (Object... objects) -> Collections.EMPTY_LIST);

    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.executeQueryThatHasResultSet(sql,
                resultSet -> {
                    User user = null;
                    if (resultSet.next()) {
                        user = new User(resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                    }
                    return user;
                },
                (Object... objects) -> {
                    List<Object> parameters = new ArrayList<>(Arrays.asList(objects));
                    return parameters;
                }, userId);
    }

}
