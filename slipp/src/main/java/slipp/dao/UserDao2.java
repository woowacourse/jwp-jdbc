package slipp.dao;

import java.util.List;

import nextstep.jdbc.JdbcTemplate;
import slipp.domain.User;

public class UserDao2 {
    private final JdbcTemplate JdbcTemplate;

    public UserDao2() {
        this.JdbcTemplate = new JdbcTemplate();
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        JdbcTemplate.updateTemplate(sql, (pstmt) -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";
        JdbcTemplate.updateTemplate(sql, (pstmt) -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";
        return JdbcTemplate.selectTemplate(sql, (pstmt) -> {}, (rs) -> new User(rs.getString("userId"),
                rs.getString("password"), rs.getString("name"), rs.getString("email")));
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return JdbcTemplate.selectTemplate(sql,
                (pstmt) -> pstmt.setString(1, userId),
                (rs) -> new User(rs.getString("userId"),
                        rs.getString("password"), rs.getString("name"),
                        rs.getString("email"))).get(0);
    }
}
