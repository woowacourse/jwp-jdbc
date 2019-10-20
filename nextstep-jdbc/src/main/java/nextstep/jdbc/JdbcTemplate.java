package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import nextstep.jdbc.rowmapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private DataSource dataSource;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionFailedException(e);
        }
    }

    public void executeUpdate(String sql, PreparedStatementSetter preparedStatementSetter) {
        executeSingle(sql, preparedStatementSetter, null);
    }

    public <T> T querySingle(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        return executeSingle(sql, preparedStatementSetter, rowMapper);
    }

    public <T> T querySingle(String sql, PreparedStatementSetter preparedStatementSetter, Class<T> clazz) {
        return executeSingle(sql, preparedStatementSetter, new ReflectionRowMapper<>(clazz));
    }

    public <T> List<T> queryMultiple(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        return executeMultiple(sql, preparedStatementSetter, rowMapper);
    }

    public <T> List<T> queryMultiple(String sql, PreparedStatementSetter preparedStatementSetter, Class<T> clazz) {
        return executeMultiple(sql, preparedStatementSetter, new ReflectionRowMapper<>(clazz));
    }


    private <T> T executeSingle(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setPreparedStatement(pstmt);
            if (pstmt.execute()) {
                return ResultSetExtractor.getInstance().extractSingle(pstmt.getResultSet(), rowMapper);
            }
            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }

    private <T> List<T> executeMultiple(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setPreparedStatement(pstmt);
            return ResultSetExtractor.getInstance().extractMultiple(pstmt.executeQuery(), rowMapper);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }
}
