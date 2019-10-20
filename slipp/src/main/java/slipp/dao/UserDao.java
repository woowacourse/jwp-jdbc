package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.mapper.ListMapper;
import nextstep.jdbc.mapper.ObjectMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private UserDao() {
    }

    public static UserDao getInstance() {
        return UserDaoHolder.instance;
    }

    public void insert(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        jdbcTemplate.updateQuery(query, pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });
    }

    public void update(User user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        String query = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";

        jdbcTemplate.updateQuery(query, pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        });
    }

    public List<User> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        String query = "SELECT userId, password, name, email FROM USERS";

        return jdbcTemplate.executeQuery(query,
                pstmt -> {
                }, new ListMapper<>(
                        rs -> new User(
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email"))
                )
        );
    }

    public User findByUserId(String userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getConnection());
        String query = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        return jdbcTemplate.executeQuery(query,
                pstmt -> {
                    pstmt.setString(1, userId);
                }, new ObjectMapper<>(
                        rs -> new User(
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email"))
                )
        );
    }

    private static class UserDaoHolder {
        static UserDao instance = new UserDao();
    }
}
