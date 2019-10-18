package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import nextstep.jdbc.resultsetextractionstrategy.MultipleEntitiesResultSetExtractionStrategy;
import nextstep.jdbc.resultsetextractionstrategy.ResultSetExtractionStrategy;
import nextstep.jdbc.resultsetextractionstrategy.SingleEntityResultSetExtractionStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate {

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
        execute(sql, preparedStatementSetter, null);
    }

    public <T> T queryForSingleEntity(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        return execute(sql, preparedStatementSetter, new SingleEntityResultSetExtractionStrategy<>(rowMapper));
    }

    public <T> List<T> queryForMultipleEntities(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        return execute(sql, preparedStatementSetter, new MultipleEntitiesResultSetExtractionStrategy<>(rowMapper));
    }

    private <U> U execute(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetExtractionStrategy<U> resultSetExtractionStrategy) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setPreparedStatement(pstmt);
            if (pstmt.execute() && resultSetExtractionStrategy != null) {
                return resultSetExtractionStrategy.extract(pstmt.getResultSet());
            }
            return null;
        } catch (SQLException e) {
            throw new ExecuteUpdateFailedException(e);
        }
    }
}
