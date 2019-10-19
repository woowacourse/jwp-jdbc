package slipp.dao;

import nextstep.jdbc.template.JdbcTemplate;
import nextstep.jdbc.template.RowMapper;
import slipp.domain.User;

import java.util.List;

public class UserDao {

    private static final String USER_INSERT_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE USERS SET PASSWORD = ?, NAME = ?, EMAIL = ? WHERE USERID = ?";
    private static final String FIND_ALL_USER_QUERY = "SELECT * FROM USERS";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate();
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
        jdbcTemplate.save(USER_INSERT_QUERY, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.save(UPDATE_USER_QUERY, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_USER_QUERY, rowMapper);
    }

    public User findUserById(String userId) {
        return jdbcTemplate.queryForObject(FIND_USER_BY_ID_QUERY, rowMapper, userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}
