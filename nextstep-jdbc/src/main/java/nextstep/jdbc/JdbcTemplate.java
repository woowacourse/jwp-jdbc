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
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        };
        return execute(sql, preparedStatementSetter, strategy);
    }

    private <T> T execute(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetExtractionStrategy<T> strategy) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            preparedStatementSetter.setParameters(pstmt);
            return extractEntity(pstmt, strategy);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private <T> T extractEntity(PreparedStatement pstmt, ResultSetExtractionStrategy<T> strategy) throws SQLException {
        if (!pstmt.execute() || strategy == null) {
            return null;
        }
        try (ResultSet rs = pstmt.getResultSet()) {
            return strategy.extract(rs);
        }
    }
}
