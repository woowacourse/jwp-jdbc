package nextstep.jdbc;

import nextstep.jdbc.exception.ExecuteUpdateSQLException;
import nextstep.jdbc.exception.NotOnlyResultException;
import nextstep.jdbc.exception.SelectSQLException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(String sql, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ExecuteUpdateSQLException();
        }
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private PreparedStatementSetter getDefaultPreparedStatementSetter(Object[] values) {
        return pstmt -> {
            for (int i = 1; i <= values.length; i++) {
                pstmt.setObject(i, values[i]);
            }
        };
    }

    public void execute(String sql, Object... values) {
        execute(sql, getDefaultPreparedStatementSetter(values));
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

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
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
        return query(sql, rowMapper, getDefaultPreparedStatementSetter(values));
    }


    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pss) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pss.setValues(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            return getResult(rowMapper, resultSet);
        } catch (SQLException e) {
            throw new SelectSQLException();
        }
    }

    private <T> Optional<T> getResult(RowMapper<T> rowMapper, ResultSet resultSet) throws SQLException {
        Optional<T> result = Optional.empty();
        while (resultSet.next()) {
            if (result.isPresent()) {
                throw new NotOnlyResultException();
            }

            result = Optional.of(rowMapper.mapRow(resultSet));
        }

        return result;
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... values) {
        return queryForObject(sql, rowMapper, getDefaultPreparedStatementSetter(values));
    }
}
