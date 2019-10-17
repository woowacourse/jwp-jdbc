package slipp.dao;

import nextstep.jdbc.template.JdbcTemplate;
import nextstep.jdbc.template.RowMapper;
import slipp.domain.User;

import java.util.List;

public class UserDao {

    private final JdbcTemplate<User> jdbcTemplate = new JdbcTemplate<>();
    private final RowMapper<User> rowMapper = new RowMapper<>(User.class);

    private UserDao() {
    }

    public static UserDao getInstance() {
        return UserDaoHolder.INSTANCE;
    }

    private static class UserDaoHolder {
        private static final UserDao INSTANCE = new UserDao();
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.save(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET PASSWORD = ?, NAME = ?, EMAIL = ? WHERE USERID = ?";
        jdbcTemplate.save(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this.rowMapper);
    }

    public User findUserById(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}
