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

public abstract class JdbcTemplate {

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

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            return getResults(rowMapper, resultSet);
        } catch (SQLException e) {
            throw new SelectSQLException();
        }
    }

    private <T> List<T> getResults(RowMapper<T> rowMapper, ResultSet resultSet) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            results.add(rowMapper.mapRow(resultSet));
        }

        return results;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... values) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setObjects(pstmt, values);
            ResultSet resultSet = pstmt.executeQuery();

            return getResults(rowMapper, resultSet);
        } catch (SQLException e) {
            throw new SelectSQLException();
        }
    }


    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            return getResult(rowMapper, resultSet);
        } catch (SQLException e) {
            throw new SelectSQLException();
        }
    }

    private <T> T getResult(RowMapper<T> rowMapper, ResultSet resultSet) throws SQLException {
        T result = null;
        while (resultSet.next()) {
            if (result != null) {
                throw new NotOnlyResultException();
            }

            result = rowMapper.mapRow(resultSet);
        }

        return result;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setObjects(pstmt, values);
            ResultSet resultSet = pstmt.executeQuery();

            return getResult(rowMapper, resultSet);
        } catch (SQLException e) {
            throw new SelectSQLException();
        }
    }

    private void setObjects(PreparedStatement pstmt, Object[] values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i, values[i]);
        }
    }

    protected abstract Connection getConnection();

}
