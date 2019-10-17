package nextstep.jdbc;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public void insert(String sql, PrepareStatementSetter prepareStatementSetter) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = ConnectionManager.getConnection();
            preparedStatement = con.prepareStatement(sql);
            prepareStatementSetter.setParameters(preparedStatement);

            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    public User objectQuery(String sql, PrepareStatementSetter prepareStatementSetter, RowMapper rowMapper) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            con = ConnectionManager.getConnection();
            preparedStatement = con.prepareStatement(sql);
            prepareStatementSetter.setParameters(preparedStatement);
            resultSet = preparedStatement.executeQuery();

            return rowMapper.mapRow(resultSet);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public List<User> listQuery(String sql, RowMapper rowMapper) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = rowMapper.mapRow(resultSet);
                users.add(user);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return users;
    }
}
