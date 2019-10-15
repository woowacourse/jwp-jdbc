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

        excuteAll(sql, null, values);
    }

    public <T> T executeQuery(final String sql, RowMapper<T> rowMapper, Object... values) {
        log.debug("executeQuery sql={}", sql);

        return (T) excuteAll(sql, rowMapper, values);
    }

    public <T> Object excuteAll(final String sql, RowMapper<T> rowMapper, Object... values) {
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
}
