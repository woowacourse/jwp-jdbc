package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());

    private static class LazyHolder {
        private static final UserDao userDao = new UserDao();
    }

    public static UserDao getInstance() {
        return LazyHolder.userDao;

    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PreparedStatementSetter preparedStatementSetter= pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

        };
        jdbcTemplate.executeUpdate(sql, preparedStatementSetter);
//        jdbcTemplate.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };
//        jdbcTemplate.executeUpdate(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
        jdbcTemplate.executeUpdate(sql, preparedStatementSetter);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {

        };
        RowMapper<User> rowMapper = makeRowMapper();
        return jdbcTemplate.queryForMultipleEntities(sql, preparedStatementSetter, rowMapper);
    }

    public User findByUserId(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        PreparedStatementSetter preparedStatementSetter = pstmt -> {
            pstmt.setString(1, userId);
        };
        RowMapper<User> rowMapper = makeRowMapper();
//        return jdbcTemplate.queryForSingleEntity(sql, rowMapper, userId);
        return jdbcTemplate.queryForSingleEntity(sql, preparedStatementSetter, rowMapper);
    }

    private RowMapper<User> makeRowMapper() {
        return rs -> {
            String retrievedUserId = rs.getString("userId");
            String password = rs.getString("password");
            String name = rs.getString("name");
            String email = rs.getString("email");

            return new User(retrievedUserId, password, name, email);
        };
    }
}
