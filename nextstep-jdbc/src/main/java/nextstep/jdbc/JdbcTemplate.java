package nextstep.jdbc;

import nextstep.jdbc.exception.ExecuteUpdateSQLException;
import nextstep.jdbc.exception.NotOnlyResultException;
import nextstep.jdbc.exception.SelectSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate<T> {

    public void execute(String sql, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ExecuteUpdateSQLException();
        }
    }

    public void execute(String sql, Object... values) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setObjects(pstmt, values);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ExecuteUpdateSQLException();
        }
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        List<T> result = new ArrayList<>();
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new SelectSQLException();
        }

        return result;
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, Object... values) {
        List<T> result = new ArrayList<>();
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setObjects(pstmt, values);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new SelectSQLException();
        }

        return result;
    }


    public T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        T result = null;

        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                if (result != null) {
                    throw new NotOnlyResultException();
                }

                result = rowMapper.mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new SelectSQLException();
        }

        return result;
    }

    public T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        T result = null;

        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setObjects(pstmt, values);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                if (result != null) {
                    throw new NotOnlyResultException();
                }

                result = rowMapper.mapRow(resultSet);
            }
        } catch (SQLException e) {
            throw new SelectSQLException();
        }

        return result;
    }

    private void setObjects(PreparedStatement pstmt, Object[] values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i, values[i]);
        }
    }

    protected abstract Connection getConnection();

}
