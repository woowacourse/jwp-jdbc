package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.NamedParameterJdbcTemplate;
import nextstep.jdbc.PropertyRowMapper;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.exception.UserNotFoundException;
import slipp.support.db.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<User> rowMapper = PropertyRowMapper.from(User.class);

    private UserDao() {
        this.jdbcTemplate = new JdbcTemplate(DataSource.getDataSource());
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataSource.getDataSource());
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final List<Object> params = List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        jdbcTemplate.update(sql, params);
    }

    public void update(User user) {
        final String sql = "UPDATE USERS SET password = :password, name = :name, email = :email WHERE userId = :userId";
        final Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("email", user.getEmail());
        params.put("userId", user.getUserId());
        params.put("password", user.getPassword());
        namedParameterJdbcTemplate.update(sql, params);
    }

    public List<User> findAll() {
        final String sql = "SELECT * FROM USERS";
        return jdbcTemplate.executeForList(sql, rowMapper);
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userId=:userId";
        final Map<String, Object> params = Map.of("userId", userId);
        return namedParameterJdbcTemplate.executeForObject(sql, params, rowMapper)
                .orElseThrow(UserNotFoundException::new);
    }

    public void deleteByUserId(final String userId) {
        final String sql = "DELETE FROM USERS WHERE userId = :userId";
        final Map<String, Object> params = Map.of("userId", userId);
        namedParameterJdbcTemplate.update(sql, params);
    }
}
