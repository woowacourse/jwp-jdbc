package nextstep.jdbc;

import nextstep.jdbc.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate<T> {
    private final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    private final JdbcExecutor jdbcExecutor;

    public JdbcTemplate() {
        this.jdbcExecutor = new JdbcExecutor();
    }

    public int save(String sql, Object... objects) {
        return jdbcExecutor.execute(sql, pstmt -> executeUpdate(pstmt, objects));
    }

    private int executeUpdate(PreparedStatement pstmt, Object[] objects) throws SQLException {
        setValues(pstmt, objects);
        return pstmt.executeUpdate();
    }

    private void setValues(PreparedStatement pstmt, Object[] objects) {
        for (int index = 1; index <= objects.length; index++) {
            setString(pstmt, objects[index - 1], index);
        }
    }

    private void setString(PreparedStatement pstmt, Object object, int index) {
        try {
            pstmt.setString(index, String.valueOf(object));
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }

    public List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) {
        return jdbcExecutor.execute(sql, pstmt -> executeQuery(rowMapper, pstmt, objects));
    }

    private List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatement pstmt, Object[] objects) {
        setValues(pstmt, objects);
        return ResultSetHelper.getData(rowMapper, pstmt);
    }

    public Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... objects) {
        return jdbcExecutor.execute(sql, pstmt -> executeQueryForObject(rowMapper, pstmt, objects));
    }

    private Optional<T> executeQueryForObject(RowMapper<T> rowMapper, PreparedStatement pstmt, Object[] objects) {
        setValues(pstmt, objects);
        return Optional.ofNullable(ResultSetHelper.getData(rowMapper, pstmt).get(0));
    }
}
