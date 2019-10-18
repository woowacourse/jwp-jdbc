package slipp.dao;

import nextstep.jdbc.mapper.ListMapper;
import nextstep.jdbc.mapper.ObjectMapper;
import nextstep.jdbc.query.PreparedStatementBuilder;
import nextstep.jdbc.template.JdbcTemplate;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private UserDao() {
    }

    public static UserDao getInstance() {
        return UserDaoHolder.instance;
    }

    public void insert(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        PreparedStatementBuilder preparedStatementBuilder = new PreparedStatementBuilder(
                "INSERT INTO USERS VALUES (?, ?, ?, ?)");
        preparedStatementBuilder.addAttribute(user.getUserId())
                .addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail());

        jdbcTemplate.updateQuery(preparedStatementBuilder);
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        PreparedStatementBuilder preparedStatementBuilder = new PreparedStatementBuilder(
                "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?");
        preparedStatementBuilder.addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail())
                .addAttribute(user.getUserId());

        jdbcTemplate.updateQuery(preparedStatementBuilder);
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        PreparedStatementBuilder preparedStatementBuilder = new PreparedStatementBuilder(
                "SELECT userId, password, name, email FROM USERS");

        return jdbcTemplate.executeQuery(preparedStatementBuilder, new ListMapper<User>() {

            @Override
            protected User createRow(ResultSet resultSet) throws SQLException {
                return new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            }
        });
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        PreparedStatementBuilder preparedStatementBuilder = new PreparedStatementBuilder(
                "SELECT userId, password, name, email FROM USERS WHERE userid=?");
        preparedStatementBuilder.addAttribute(userId);

        return jdbcTemplate.executeQuery(preparedStatementBuilder, new ObjectMapper<User>() {

            @Override
            protected User createRow(ResultSet resultSet) throws SQLException {
                return new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            }
        });
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(ConnectionManager.getConnection());
    }

    private static class UserDaoHolder {
        static UserDao instance = new UserDao();
    }
}
