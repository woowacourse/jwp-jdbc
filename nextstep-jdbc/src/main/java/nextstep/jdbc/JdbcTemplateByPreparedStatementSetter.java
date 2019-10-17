package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplateByPreparedStatementSetter {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateByPreparedStatementSetter.class);

    public static int save(String sql, PreparedStatementSetter pstmtSetter) {
        return JdbcConnector.execute(sql, pstmt -> executeUpdate(pstmtSetter, pstmt));
    }

    private static int executeUpdate(PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt.executeUpdate();
    }

    public static <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return JdbcConnector.execute(sql, pstmt -> executeQuery(rowMapper, pstmtSetter, pstmt));
    }

    private static <T> List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return executeQuery(rowMapper, pstmt);
    }

    private static <T> List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatement pstmt) {
        try (ResultSet resultSet = pstmt.executeQuery()) {
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(rowMapper.mapRow(resultSet));
            }
            return results;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    public static <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return JdbcConnector.execute(sql, pstmt -> executeQueryForObject(rowMapper, pstmtSetter, pstmt));
    }

    private static <T> Optional<T> executeQueryForObject(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return Optional.ofNullable(executeQuery(rowMapper, pstmt).get(0));
    }
}
