package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... args) {
        execute(sql, null, args);
    }

    public <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        return execute(sql, new MultipleResultSetExtractionStrategy<>(rowMapper), args);
    }

    public <T> T executeQueryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        return execute(sql, new SingleResultSetExtractionStrategy<>(rowMapper), args);
    }

    private <T> T execute(String sql, ResultSetExtractionStrategy<T> strategy, Object... args) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            return extractEntity(strategy, pstmt);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private <T> T extractEntity(ResultSetExtractionStrategy<T> strategy, PreparedStatement pstmt) throws SQLException {
        if (pstmt.execute() && strategy != null) {
            try (ResultSet rs = pstmt.getResultSet()) {
                return strategy.extract(rs);
            }
        }
        return null;
    }

    private <T> T execute(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetExtractionStrategy<T> strategy) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            preparedStatementSetter.setValues(pstmt);

            return extractEntity(strategy, pstmt);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }
}
