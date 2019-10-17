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

    public void insert(String sql, Object ... objects) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = ConnectionManager.getConnection();
            preparedStatement = con.prepareStatement(sql);

            setPrepareStatement(preparedStatement, objects);
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

    public <T> T objectQuery(String sql, PrepareStatementSetter prepareStatementSetter, RowMapper<T> rowMapper) throws SQLException {
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

    public <T> T objectQuery(String sql, RowMapper<T> rowMapper, Object... objects) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            con = ConnectionManager.getConnection();
            preparedStatement = con.prepareStatement(sql);

            setPrepareStatement(preparedStatement, objects);

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

    public <T> List<T> listQuery(String sql, RowMapper<T> rowMapper) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> objects = new ArrayList<>();
        try {
            con = ConnectionManager.getConnection();
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                objects.add(rowMapper.mapRow(resultSet));
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
        return objects;
    }

    private void setPrepareStatement(PreparedStatement preparedStatement, Object[] objects) throws SQLException {
        for (int i = 0; i < objects.length; i++) {
            preparedStatement.setObject(i + 1, objects[i]);
        }
    }
}
