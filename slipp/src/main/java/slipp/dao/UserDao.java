package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;

import java.util.List;

public class UserDao {

    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "UPDATE USERS SET (password, name, email) = (?,?,?) WHERE userId = ?";
        jdbcTemplate.update(sql,
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.query(sql,
                rs -> new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")));
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return (User) jdbcTemplate.queryForObject(sql,
                rs -> new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")),
                pstmt -> pstmt.setString(1, userId));
    }
}
