package slipp.dao;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(new ConnectionManager("testdb.properties").getDataSource());

    public void insert(User user) {
        String insertSql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?";
        jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";

        return jdbcTemplate.query(sql,
                pstmt -> {
                },
                this::getUser);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.queryForObject(sql,
                pstmt -> pstmt.setString(1, userId),
                this::getUser);
    }

    private User getUser(ResultSet rs) throws SQLException {
        return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));
    }
}
