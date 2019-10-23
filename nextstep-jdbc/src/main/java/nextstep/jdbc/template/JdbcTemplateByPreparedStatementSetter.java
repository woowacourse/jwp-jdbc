package nextstep.jdbc.template;

import nextstep.jdbc.db.ConnectionManager;
import nextstep.jdbc.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcTemplateByPreparedStatementSetter {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTemplateByPreparedStatementSetter.class);

    public int save(String sql, PreparedStatementSetter pstmtSetter) {
        return execute(sql, PreparedStatement::executeUpdate, pstmtSetter);
    }

    public <T> T execute(String sql, PreparedStatementHandler<T> handler, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            return handler.handle(pstmt);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return execute(sql, pstmt -> ResultSetHelper.getData(rowMapper, pstmt), pstmtSetter);
    }

    public <T> Optional<T> queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter pstmtSetter) {
        return Optional.ofNullable(query(sql, rowMapper, pstmtSetter).get(0));
    }
}
