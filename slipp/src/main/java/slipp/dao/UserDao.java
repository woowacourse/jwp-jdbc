package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.SqlMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        SqlMapper sqlMapper = new SqlMapper("INSERT INTO USERS VALUES (?, ?, ?, ?)");
        sqlMapper.addAttribute(user.getUserId())
                .addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail());

        jdbcTemplate.updateQuery(sqlMapper);
    }

    public void update(User user) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        SqlMapper sqlMapper = new SqlMapper("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?");
        sqlMapper.addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail())
                .addAttribute(user.getUserId());

        jdbcTemplate.updateQuery(sqlMapper);
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        SqlMapper sqlMapper = new SqlMapper("SELECT userId, password, name, email FROM USERS");

        return jdbcTemplate.executeQuery(sqlMapper,
                resultSet -> {
                    List<User> users = new ArrayList<>();
                    while (resultSet.next()) {
                        User user = new User(
                                resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                        users.add(user);
                    }

                    return users;
                });
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        SqlMapper sqlMapper = new SqlMapper("SELECT userId, password, name, email FROM USERS WHERE userid=?");
        sqlMapper.addAttribute(userId);

        return jdbcTemplate.executeQuery(sqlMapper,
                resultSet -> {
                    User user = null;
                    if (resultSet.next()) {
                        user = new User(
                                resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                    }

                    return user;
                });
    }
}
