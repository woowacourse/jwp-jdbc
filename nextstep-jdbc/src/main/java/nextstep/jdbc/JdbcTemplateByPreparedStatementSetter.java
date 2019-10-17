package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplateByPreparedStatementSetter {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateByPreparedStatementSetter.class);

    public static int save(String sql, PreparedStatementSetter pstmtSetter) {
        return JdbcExecutor.execute(sql, pstmt -> executeUpdate(pstmtSetter, pstmt));
    }

    private static int executeUpdate(PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt.executeUpdate();
    }

    public static <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return JdbcExecutor.execute(sql, pstmt -> executeQuery(rowMapper, pstmtSetter, pstmt));
    }

    private static <T> List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return ResultSetHelper.getData(rowMapper, pstmt);
    }

    public static <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return JdbcExecutor.execute(sql, pstmt -> executeQueryForObject(rowMapper, pstmtSetter, pstmt));
    }

    private static <T> Optional<T> executeQueryForObject(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return Optional.ofNullable(ResultSetHelper.getData(rowMapper, pstmt).get(0));
    }
}
