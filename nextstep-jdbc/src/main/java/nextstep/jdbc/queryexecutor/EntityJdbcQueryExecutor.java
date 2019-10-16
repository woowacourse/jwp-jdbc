package nextstep.jdbc.queryexecutor;

import nextstep.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntityJdbcQueryExecutor implements JdbcQueryExecutor {
    private static final Logger log = LoggerFactory.getLogger(EntityJdbcQueryExecutor.class);

    @Override
    public Boolean canHandle(String sql) {
        boolean truth = sql.startsWith("SELECT");
        return truth;
    }

    @Override
    public <T> List<T> execute(PreparedStatement pstmt, RowMapper<T> rowMapper) {
        try {
            return executeRowMapper(pstmt, rowMapper);
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private <T> List<T> executeRowMapper(final PreparedStatement pstmt, final RowMapper<T> rowMapper) throws SQLException {
        try (ResultSet rs = pstmt.executeQuery()) {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(rowMapper.mapRow(rs));
            }
            return objects;
        }
    }
}
