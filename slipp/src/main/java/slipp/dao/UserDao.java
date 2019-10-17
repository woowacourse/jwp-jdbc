package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.mapper.ListMapper;
import nextstep.jdbc.mapper.ObjectMapper;
import nextstep.jdbc.query.SqlMapper;
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
        SqlMapper sqlMapper = new SqlMapper("INSERT INTO USERS VALUES (?, ?, ?, ?)");
        sqlMapper.addAttribute(user.getUserId())
                .addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail());

        jdbcTemplate.updateQuery(sqlMapper);
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        SqlMapper sqlMapper = new SqlMapper("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?");
        sqlMapper.addAttribute(user.getPassword())
                .addAttribute(user.getName())
                .addAttribute(user.getEmail())
                .addAttribute(user.getUserId());

        jdbcTemplate.updateQuery(sqlMapper);
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        SqlMapper sqlMapper = new SqlMapper("SELECT userId, password, name, email FROM USERS");

        return jdbcTemplate.executeQuery(sqlMapper, new ListMapper<User>() {

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
        SqlMapper sqlMapper = new SqlMapper("SELECT userId, password, name, email FROM USERS WHERE userid=?");
        sqlMapper.addAttribute(userId);

        return jdbcTemplate.executeQuery(sqlMapper, new ObjectMapper<User>() {

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
