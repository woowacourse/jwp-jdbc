package slipp.dao;


import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PrepareStatementSetter;
import nextstep.jdbc.RowMapper;
import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        PrepareStatementSetter prepareStatementSetter = new PrepareStatementSetter() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getEmail());
            }
        };
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.insert(sql, prepareStatementSetter);
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE USERS SET password=?,name=?,email=? WHERE userId=?";
        PrepareStatementSetter prepareStatementSetter = new PrepareStatementSetter() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, user.getPassword());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getUserId());
            }
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.insert(sql, prepareStatementSetter);
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM USERS";
        RowMapper rowMapper = new RowMapper() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email"));
            }
        };

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.listQuery(sql, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        PrepareStatementSetter prepareStatementSetter = new PrepareStatementSetter() {
            @Override
            public void setParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, userId);
            }
        };

        RowMapper rowMapper = new RowMapper() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                User user = null;
                if (rs.next()) {
                    user = new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
                }
                return user;
            }
        };
        
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.objectQuery(sql ,prepareStatementSetter, rowMapper);
    }
}
