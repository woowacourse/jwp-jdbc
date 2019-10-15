package nextstep.jdbc.queryexecutor;

import nextstep.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityJdbcQueryExecutor implements JdbcQueryExecutor {
    private static final Logger log = LoggerFactory.getLogger(EntityJdbcQueryExecutor.class);

    @Override
    public Boolean canHandle(String sql) {
        boolean truth = sql.startsWith("SELECT");
        return truth;
    }

    @Override
    public Object execute(PreparedStatement pstmt, RowMapper rowMapper) {
        try {
            return executeRowMapper(pstmt, rowMapper);
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private <T> T executeRowMapper(final PreparedStatement pstmt, final RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            return rowMapper.mapRow(rs);
        }
    }
}
