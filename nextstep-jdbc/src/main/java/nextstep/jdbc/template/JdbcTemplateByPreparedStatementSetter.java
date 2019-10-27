package nextstep.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Deprecated
public class JdbcTemplateByPreparedStatementSetter<T> {
    private final JdbcExecutor jdbcExecutor;

    public JdbcTemplateByPreparedStatementSetter() {
        this.jdbcExecutor = new JdbcExecutor();
    }

    public int save(String sql, PreparedStatementSetter pstmtSetter) {
        return jdbcExecutor.execute(sql, pstmt -> executeUpdate(pstmtSetter, pstmt));
    }

    private int executeUpdate(PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt.executeUpdate();
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return jdbcExecutor.execute(sql, pstmt -> executeQuery(rowMapper, pstmtSetter, pstmt));
    }

    private List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return ResultSetHelper.getData(rowMapper, pstmt);
    }

    public Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return jdbcExecutor.execute(sql, pstmt -> executeQueryForObject(rowMapper, pstmtSetter, pstmt));
    }

    private Optional<T> executeQueryForObject(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return maybeObject(ResultSetHelper.getData(rowMapper, pstmt));
    }

    private Optional<T> maybeObject(List<T> list) {
        if (list.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }
}
