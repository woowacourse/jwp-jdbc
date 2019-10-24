package slipp.dao;


import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.exception.UserNotFoundException;

import java.util.List;

public class UserDao {
    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:jwp-framework";
    public static final String DB_USERNAME = "sa";
    public static final String DB_PW = "";
    private JdbcTemplate jdbcTemplate;

    public UserDao() {
        jdbcTemplate = new JdbcTemplate(ConnectionGenerator.getDataSource(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW));
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
            rs.getString("email"));

        return jdbcTemplate.listQuery(sql, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
            rs.getString("email"));

        return jdbcTemplate.singleObjectQuery(sql, rowMapper, userId).orElseThrow(UserNotFoundException::new);
    }
}
