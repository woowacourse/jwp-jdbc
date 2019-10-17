package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    public static int save(String sql, PreparedStatementSetter pstmtSetter) {
        return JdbcConnector.execute(sql, pstmt -> executeUpdate(pstmtSetter, pstmt));
    }

    private static int executeUpdate(PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt.executeUpdate();
    }

    public static int save(String sql, Object... objects) {
        return JdbcConnector.execute(sql, pstmt -> executeUpdate(pstmt, objects));
    }

    private static int executeUpdate(PreparedStatement pstmt, Object[] objects) throws SQLException {
        setValues(pstmt, objects);
        return pstmt.executeUpdate();
    }

    private static void setValues(PreparedStatement pstmt, Object[] objects) {
        try {
            for (int i = 1; i <= objects.length; i++) {
                pstmt.setString(i, String.valueOf(objects[i - 1]));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

    public static <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return JdbcConnector.execute(sql, pstmt -> executeQuery(rowMapper, pstmt));
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

    public static <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return JdbcConnector.execute(sql, pstmt -> executeQuery(rowMapper, pstmtSetter, pstmt));
    }

    private static <T> List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return executeQuery(rowMapper, pstmt);
    }

    public static <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) {
        return JdbcConnector.execute(sql, pstmt -> executeQuery(rowMapper, pstmt, objects));
    }

    private static <T> List<T> executeQuery(RowMapper<T> rowMapper, PreparedStatement pstmt, Object[] objects) {
        setValues(pstmt, objects);
        return executeQuery(rowMapper, pstmt);
    }

    public static <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return JdbcConnector.execute(sql, pstmt -> executeQueryForObject(rowMapper, pstmtSetter, pstmt));
    }

    private static <T> Optional<T> executeQueryForObject(RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter, PreparedStatement pstmt) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return Optional.ofNullable(executeQuery(rowMapper, pstmt).get(0));
    }

    public static <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, Object... objects) {
        return JdbcConnector.execute(sql, pstmt -> executeQueryForObject(rowMapper, pstmt, objects));
    }

    private static <T> Optional<T> executeQueryForObject(RowMapper<T> rowMapper, PreparedStatement pstmt, Object[] objects) {
        setValues(pstmt, objects);
        return Optional.ofNullable(executeQuery(rowMapper, pstmt).get(0));
    }
}
