package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import nextstep.jdbc.queryexecutor.EntityJdbcQueryExecutor;
import nextstep.jdbc.queryexecutor.JdbcQueryExecutor;
import nextstep.jdbc.queryexecutor.UpdateJdbcQueryExecutor;
import nextstep.jdbc.resultsetextractionstrategy.MultipleEntitiesResultSetExtractionStrategy;
import nextstep.jdbc.resultsetextractionstrategy.ResultSetExtractionStrategy;
import nextstep.jdbc.resultsetextractionstrategy.SingleEntityResultSetExtractionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class JdbcTemplate {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);
    private static final int START_INDEX = 1;
    private static final int INDEX_OF_SINGLE_ENTITY = 0;

    private DataSource dataSource;
    private List<JdbcQueryExecutor> jdbcQueryExecutors;

    public JdbcTemplate(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcQueryExecutors = Arrays.asList(new UpdateJdbcQueryExecutor(), new EntityJdbcQueryExecutor());
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

    private <T> T execute(String sql, PreparedStatementSetter preparedStatementSetter, ResultSetExtractionStrategy<T> resultSetExtractionStrategy) {
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
