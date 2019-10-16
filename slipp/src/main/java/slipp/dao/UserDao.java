package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.exception.NotFoundUserException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public void insert(User user) throws SQLException {
        String query = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        jdbcTemplate.execute(query, pss);
    }

    public void update(User user) throws SQLException {
        String query = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };

        jdbcTemplate.execute(query, pss);
    }

    public List<User> findAll() throws SQLException {
        RowMapper rm = rs -> {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                String userId = rs.getString("userId");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");
                users.add(new User(userId, password, name, email));
            }
            return users;
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query("SELECT * FROM users", rm);
    }


    public User findByUserId(String userId) throws SQLException {
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);

        RowMapper rm = rs -> {
            if (rs.next()) {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
            throw new NotFoundUserException();
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.queryForObject(userId, pss, rm);
    }
}
