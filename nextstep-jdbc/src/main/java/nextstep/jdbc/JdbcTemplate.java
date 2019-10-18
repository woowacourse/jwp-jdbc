package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import nextstep.jdbc.resultsetextractionstrategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        execute(sql, preparedStatementSetter, null);
    }

    public <T> T queryForSingleEntity(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        return execute(sql, preparedStatementSetter, new SingleEntityResultSetExtractionStrategy<>(rowMapper));
    }

    public <T> T queryForSingleEntityWithoutRowMapper(String sql, PreparedStatementSetter preparedStatementSetter, Class<T> clazz) {
        return execute(sql, preparedStatementSetter, new SingleEntityResultSetExtractionStrategyWithoutRowMapper<>(clazz));
    }

    public <T> List<T> queryForMultipleEntities(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) {
        return execute(sql, preparedStatementSetter, new MultipleEntitiesResultSetExtractionStrategy<>(rowMapper));
    }

    public <T> List<T> queryForMultipleEntitiesWithoutRowMapper(String sql, PreparedStatementSetter preparedStatementSetter, Class<T> clazz) {
        return execute(sql, preparedStatementSetter, new MultipleEntitiesResultSetExtractionStrategyWithoutRowMapper<>(clazz));
    }

    private <U> U execute(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetExtractionStrategy<U> resultSetExtractionStrategy) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            preparedStatementSetter.setPreparedStatement(pstmt);
            if (pstmt.execute() && resultSetExtractionStrategy != null) {
                return resultSetExtractionStrategy.extract(pstmt.getResultSet());
            }
            return null;
        } catch (SQLException | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }
    }
}
