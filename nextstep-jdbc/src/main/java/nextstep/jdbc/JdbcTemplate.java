package nextstep.jdbc;

import nextstep.jdbc.executeStrategy.ExecuteStrategy;
import nextstep.jdbc.executeStrategy.ModifyStrategy;
import nextstep.jdbc.executeStrategy.SelectAllStrategy;
import nextstep.jdbc.executeStrategy.SelectForObjectStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate<T> {
    public List<T> select(String query, RowMapper<T> rowMapper, Object... values) {
        return select(query, rowMapper, (pstmt -> setValues(pstmt, values)));
    }

    public List<T> select(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        ExecuteStrategy<List<T>> executeStrategy = new SelectAllStrategy<>(rowMapper);
        return executeStrategy.execute(query, pstmtSetter);
    }

    public Optional<T> selectForObject(String query, RowMapper<T> rowMapper, Object... values) {
        return selectForObject(query, rowMapper, pstmt -> setValues(pstmt, values));
    }

    public Optional<T> selectForObject(String query, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        ExecuteStrategy<T> executeStrategy = new SelectForObjectStrategy<>(rowMapper);
        return Optional.ofNullable(executeStrategy.execute(query, pstmtSetter));
    }

    public int update(String query, Object... values) {
        return update(query, pstmt -> setValues(pstmt, values));
    }

    public int update(String query, PreparedStatementSetter pstmtSetter) {
        ExecuteStrategy<Integer> executeStrategy = new ModifyStrategy();
        return executeStrategy.execute(query, pstmtSetter);
    }

    private void setValues(PreparedStatement pstmt, Object[] values) throws SQLException {
        for (int i = 1; i <= values.length; i++) {
            pstmt.setObject(i, values[i - 1]);
        }
    }
}
