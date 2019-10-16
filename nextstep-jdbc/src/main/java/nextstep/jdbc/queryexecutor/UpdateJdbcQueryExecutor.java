package nextstep.jdbc.queryexecutor;

import nextstep.jdbc.RowMapper;
import nextstep.jdbc.exception.ExecuteUpdateFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateJdbcQueryExecutor implements JdbcQueryExecutor {
    private static final Logger log = LoggerFactory.getLogger(UpdateJdbcQueryExecutor.class);

    @Override
    public Boolean canHandle(String sql) {
        boolean truth = sql.startsWith("INSERT") || sql.startsWith("UPDATE");
        return truth;
    }

    @Override
    public <T> List<T> execute(PreparedStatement ps, RowMapper<T> rowMapper) {
        try {
            ps.executeUpdate();
            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ExecuteUpdateFailedException(e);
        }

    }
}
