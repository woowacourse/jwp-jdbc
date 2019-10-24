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

    public int update(String sql, Object... args) {
        return execute(sql, PreparedStatement::executeUpdate, args);
    }

    public <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper, Object... args) {
        return execute(sql, pstmt -> {
            ResultSet resultSet = pstmt.executeQuery();
            return new MultipleResultSetExtractionStrategy<>(rowMapper).extract(resultSet);
        }, args);
    }

    public <T> T executeQueryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        return execute(sql, pstmt -> {
            ResultSet resultSet = pstmt.executeQuery();
            return new SingleResultSetExtractionStrategy<>(rowMapper).extract(resultSet);
        }, args);
    }

    private <T> T execute(String sql, PreparedStatementExecutor<T> preparedStatementExecutor, Object... args) {
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        };
        return execute(sql, preparedStatementSetter, preparedStatementExecutor);
    }

    private <T> T execute(String sql, PreparedStatementSetter preparedStatementSetter, PreparedStatementExecutor<T> preparedStatementExecutor) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            preparedStatementSetter.setParameters(pstmt);
            return preparedStatementExecutor.execute(pstmt);
        } catch (SQLException e) {
            throw new JdbcTemplateException(e);
        }
    }
}
