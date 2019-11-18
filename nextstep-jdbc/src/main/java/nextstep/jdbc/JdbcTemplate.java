package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateException;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate implements Closeable {
    private Connection connection;

    public JdbcTemplate(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }

    public void update(String sql, Object... args) {
        PreparedStatementSetter pstmtSetter = createPstmtSetter(args);
        execute(sql, pstmtSetter, null);
    }

    public <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        ResultSetExtractionStrategy<List<T>> strategy = new MultipleResultSetExtractionStrategy<>(rowMapper);
        PreparedStatementSetter pstmtSetter = createPstmtSetter(args);

        return execute(sql, pstmtSetter, strategy);
    }

    public <T> Optional<T> executeQueryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        ResultSetExtractionStrategy<T> strategy = new SingleResultSetExtractionStrategy<>(rowMapper);
        PreparedStatementSetter pstmtSetter = createPstmtSetter(args);

        T result = execute(sql, pstmtSetter, strategy);
        return Optional.ofNullable(result);
    }

    private PreparedStatementSetter createPstmtSetter(Object... args) {
        return pstmt -> {
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
        };
    }

    private <T> T execute(String sql, PreparedStatementSetter pstmtSetter,
                          ResultSetExtractionStrategy<T> strategy) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    public void update(String sql, PreparedStatementSetter pstmtSetter) {
        execute(sql, pstmtSetter, null);
    }

    public <T> List<T> executeQuery(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        ResultSetExtractionStrategy<List<T>> strategy = new MultipleResultSetExtractionStrategy<>(rowMapper);
        return execute(sql, pstmtSetter, strategy);
    }

    public <T> Optional<T> executeQueryForObject(String sql, PreparedStatementSetter pstmtSetter,
                                                 RowMapper<T> rowMapper) {
        ResultSetExtractionStrategy<T> strategy = new SingleResultSetExtractionStrategy<>(rowMapper);

        T result = execute(sql, pstmtSetter, strategy);
        return Optional.ofNullable(result);
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }
}

