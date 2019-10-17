package nextstep.jdbc;

import nextstep.jdbc.exception.ConnectionFailedException;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import nextstep.jdbc.queryexecutor.EntityJdbcQueryExecutor;
import nextstep.jdbc.queryexecutor.JdbcQueryExecutor;
import nextstep.jdbc.queryexecutor.UpdateJdbcQueryExecutor;
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

    public void executeUpdate(final String sql, Object... values) {
        log.debug("executeUpdate sql={}", sql);

        excute(sql, null, values);
    }

    public <T> T queryForSingleEntity(String sql, RowMapper<T> rowMapper, String userId) {
        List<T> entities = queryForMultipleEntities(sql, rowMapper, userId);
        return entities.get(INDEX_OF_SINGLE_ENTITY);
    }

    public <T> List<T> queryForMultipleEntities(final String sql, RowMapper<T> rowMapper, Object... values) {
        log.debug("queryForMultipleEntities sql={}", sql);

        return excute(sql, rowMapper, values);
    }

    private <T> List<T> excute(final String sql, RowMapper<T> rowMapper, Object... values) {
        log.debug("execute sql={}", sql);

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            setValuesToPreparedStatement(pstmt, values);
            JdbcQueryExecutor queryExecutor = getQueryExecutor(sql);
            return queryExecutor.execute(pstmt, rowMapper);

        } catch (SQLException e) {
            throw new ExecuteUpdateFailedException(e);
        }
    }

    private JdbcQueryExecutor getQueryExecutor(String sql) {
        return jdbcQueryExecutors.stream()
                .filter(executor -> executor.canHandle(sql))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void setValuesToPreparedStatement(final PreparedStatement pstmt, final Object[] values) throws SQLException {
        for (int index = START_INDEX; index <= values.length; index++) {
            pstmt.setObject(index, values[index - 1]);
        }
    }


    public void executeUpdate2(String sql, PreparedStatementSetter preparedStatementSetter) {
        log.debug("execute sql={}", sql);

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            preparedStatementSetter.setPreparedStatement(pstmt);
            pstmt.executeUpdate();

//            setValuesToPreparedStatement(pstmt, values);
//            JdbcQueryExecutor queryExecutor = getQueryExecutor(sql);
//            return queryExecutor.execute(pstmt, rowMapper);

        } catch (SQLException e) {
            throw new ExecuteUpdateFailedException(e);
        }

    }
}
