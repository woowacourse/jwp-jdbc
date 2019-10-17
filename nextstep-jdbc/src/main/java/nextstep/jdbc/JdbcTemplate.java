package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... args) {
        execute(sql, null, args);
    }

    private <T> T execute(String sql, ResultSetExtractionStrategy<T> strategy, Object... args) {
        PreparedStatementSetter pstmtSetter = pstmt -> {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
        };
        return execute(sql, pstmtSetter, strategy);
    }

    public <T> T execute(String sql, PreparedStatementSetter pstmtSetter,
                         ResultSetExtractionStrategy<T> strategy) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            return extractEntity(strategy, pstmt);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    private <T> T extractEntity(ResultSetExtractionStrategy<T> strategy,
                                PreparedStatement pstmt) throws SQLException {
        if (pstmt.execute() && strategy != null) {
            try (ResultSet rs = pstmt.getResultSet()) {
                return strategy.extract(rs);
            }
        }
        return null;
    }

    public <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        ResultSetExtractionStrategy<List<T>> strategy = new MultipleResultSetExtractionStrategy<>(rowMapper);
        return execute(sql, strategy, args);
    }

    public <T> Optional<T> executeQueryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        ResultSetExtractionStrategy<T> strategy = new SingleResultSetExtractionStrategy<>(rowMapper);
        T result = execute(sql, strategy, args);

        return Optional.ofNullable(result);
    }
}
