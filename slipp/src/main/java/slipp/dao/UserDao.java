package slipp.dao;

import slipp.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDao {

    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update("INSERT INTO USERS VALUES (?, ?, ?, ?)", pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.update("UPDATE USERS SET (password, name, email) = (?,?,?) WHERE userId = ?",
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        return jdbcTemplate.query("SELECT userId, password, name, email FROM USERS",
                rs -> new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")),
                pstmt -> {
                }).stream()
                .map(object -> (User) object)
                .collect(Collectors.toList())
                ;
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return (User) jdbcTemplate.queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
                rs -> new User(rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")),
                pstmt -> pstmt.setString(1, userId));
    }
}
