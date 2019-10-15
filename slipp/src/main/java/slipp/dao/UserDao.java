package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(final User user) {
        final PreparedStatementSetter setter = new PreparedStatementSetter() {
            @Override
            public void setParameters(final PreparedStatement statement) throws SQLException {
                statement.setString(1, user.getUserId());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getName());
                statement.setString(4, user.getEmail());
            }
        };
        final JdbcTemplate template = new JdbcTemplate();
        template.write("INSERT INTO USERS VALUES (?, ?, ?, ?)", setter);
    }

    public void update(final User user) {
        final PreparedStatementSetter setter = new PreparedStatementSetter() {
            @Override
            public void setParameters(final PreparedStatement statement) throws SQLException {
                statement.setString(1, user.getPassword());
                statement.setString(2, user.getName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getUserId());
            }
        };
        final JdbcTemplate template = new JdbcTemplate();
        template.write("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userid = ?", setter);
    }

    public List<User> findAll() throws SQLException {
        // TODO 구현 필요함.
        return new ArrayList<User>();
    }

    public User findByUserId(final String userId) throws SQLException {
        final PreparedStatementSetter setter = new PreparedStatementSetter() {
            @Override
            public void setParameters(final PreparedStatement statement) throws SQLException {
                statement.setString(1, userId);
            }
        };
        final RowMapper mapper = new RowMapper() {
            @Override
            public Object mapRow(final ResultSet resultSet) throws SQLException {
                User user = null;
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getString("userId"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("email"));
                }
                return user;
            }
        };
        final JdbcTemplate template = new JdbcTemplate();
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userid = ?";
        return (User) template.findItem(sql, setter, mapper);
    }
}
