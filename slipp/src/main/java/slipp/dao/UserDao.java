package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        return jdbcTemplate.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public boolean update(User user) {
        String sql = "UPDATE USERS " +
                "SET password = ?, name = ?, email = ? " +
                "WHERE userId = ?";

        return jdbcTemplate.executeUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.executeQuery(sql, (resultSet) -> {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                String userId = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                User user = new User(userId, password, name, email);
                users.add(user);
            }

            return users;
        });
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.executeQuery(sql, (resultSet) -> {
            User user = null;
            if (resultSet.next()) {
                user = new User(resultSet.getString("userId"), resultSet.getString("password"), resultSet.getString("name"),
                        resultSet.getString("email"));
            }

            return user;
        }, userId);
    }
}
