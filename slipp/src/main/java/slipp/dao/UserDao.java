package slipp.dao;


import nextstep.jdbc.ConnectionMaker;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PrepareStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:jwp-framework";
    public static final String DB_USERNAME = "sa";
    public static final String DB_PW = "";

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = createJdbcTemplate();
        jdbcTemplate.insert(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        JdbcTemplate jdbcTemplate = createJdbcTemplate();
        jdbcTemplate.insert(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        RowMapper<User> rowMapper = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
            rs.getString("email"));

        JdbcTemplate jdbcTemplate = createJdbcTemplate();
        return jdbcTemplate.listQuery(sql, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper<User> rowMapper = rs -> {
            User user = null;
            if (rs.next()) {
                user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
            }
            return user;
        };

        JdbcTemplate jdbcTemplate = createJdbcTemplate();
        return jdbcTemplate.objectQuery(sql , rowMapper, userId);
    }

    private JdbcTemplate createJdbcTemplate() {
        Connection connection = ConnectionMaker.getConnection(DB_DRIVER, DB_URL, DB_USERNAME, DB_PW);
        return new JdbcTemplate(connection);
    }
}
