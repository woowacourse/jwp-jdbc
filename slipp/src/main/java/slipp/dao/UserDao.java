package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.ListMapper;
import nextstep.jdbc.SqlMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
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
