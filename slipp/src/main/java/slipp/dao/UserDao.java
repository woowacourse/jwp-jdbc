package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.RowMapper;
import nextstep.jdbc.exception.InsertSQLException;
import nextstep.jdbc.exception.UpdateSQLException;
import slipp.dao.exception.ResultMappingException;
import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PreparedStatementSetter pss = getInsertPreparedInsertStatementSetter(user);

        jdbcTemplate.execute(sql, pss);
    }

    private PreparedStatementSetter getInsertPreparedInsertStatementSetter(User user) {
        return pstmt -> {
            try {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            } catch (SQLException e) {
                throw new InsertSQLException();
            }
        };
    }

    public void update(User user) {
        String sql = "UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?";
        PreparedStatementSetter pss = getUpdatePreparedStatementSetter(user);

        jdbcTemplate.execute(sql, pss);
    }

    private PreparedStatementSetter getUpdatePreparedStatementSetter(User user) {
        return pstmt -> {
            try {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            } catch (SQLException e) {
                throw new UpdateSQLException();
            }
        };
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatementSetter pss = pstmt -> {
        };
        RowMapper<User> rowMapper = getUserRowMapper();

        return jdbcTemplate.query(sql, rowMapper, pss);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        RowMapper<User> rowMapper = getUserRowMapper();

        return jdbcTemplate.queryForObject(sql, rowMapper, pss);
    }

    private RowMapper<User> getUserRowMapper() {
        return resultSet -> {
            try {
                return new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            } catch (SQLException e) {
                throw new ResultMappingException();
            }
        };
    }
}
