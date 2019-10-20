package nextstep.jdbc;

import nextstep.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> select(String query, RowMapper<T> rowMapper, Object... values) {
        PreparedStatementSetter pstmtSetter = new VarargsPreparedStatementSetter(values);

        return select(query, rowMapper, pstmtSetter);
    }

    public <T> List<T> select(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return executeWithPreparedStatement(
                query,
                pstmt -> {
                    pstmtSetter.setValues(pstmt);
                    return getResults(rowMapper, pstmt);
                });
    }

    private <T> List<T> getResults(RowMapper<T> rowMapper, PreparedStatement pstmt) {
        return executeWithResultSet(
                pstmt,
                resultSet -> {
                    List<T> results = new ArrayList<>();
                    while (resultSet.next()) {
                        results.add(rowMapper.mapRow(resultSet));
                    }
                    return results;
                });
    }

    public int update(String query, PreparedStatementSetter pstmtSetter) {
        return executeWithPreparedStatement(
                query,
                pstmt -> {
                    pstmtSetter.setValues(pstmt);
                    return pstmt.executeUpdate();
                });
    }

    public int update(String query, Object... values) {
        PreparedStatementSetter pstmtSetter = new VarargsPreparedStatementSetter(values);

        return update(query, pstmtSetter);
    }

    public <T> T selectForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return executeWithPreparedStatement(
                query,
                pstmt -> {
                    pstmtSetter.setValues(pstmt);
                    return execute(pstmt, rowMapper);
                });
    }

    public <T> T selectForObject(String query, RowMapper<T> rowMapper, Object... values) {
        PreparedStatementSetter pstmtSetter = new VarargsPreparedStatementSetter(values);

        return selectForObject(query, rowMapper, pstmtSetter);
    }

    private <T> T execute(PreparedStatement pstmt, RowMapper<T> rowMapper) {
        return executeWithResultSet(
                pstmt,
                resultSet -> {
                    if (resultSet.next()) {
                        return rowMapper.mapRow(resultSet);
                    }
                    return null;
                });
    }

    private <T> T executeWithPreparedStatement(String query, Execution<PreparedStatement, T> handler) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            return handler.execute(pstmt);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e);
        }
    }

    private <T> T executeWithResultSet(PreparedStatement pstmt, Execution<ResultSet, T> execution) {
        try (ResultSet resultSet = pstmt.executeQuery()) {
            return execution.execute(resultSet);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e);
        }
    }
}
