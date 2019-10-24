package slipp.dao;

import nextstep.jdbc.*;
import slipp.domain.User;
import slipp.exception.UserNotFoundException;
import slipp.support.db.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoImpl implements UserDao {
    private static final UserDaoImpl INSTANCE = new UserDaoImpl();

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<User> rowMapper = PropertyRowMapper.from(User.class);

    private UserDaoImpl() {
        this.jdbcTemplate = new JdbcTemplate(DataSource.getDataSource());
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataSource.getDataSource());
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        final PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(
                user.getUserId(),
                user.getPassword(),
                user.getName(),
                user.getEmail());
        jdbcTemplate.update(sql, pss);
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
