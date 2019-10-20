package nextstep.jdbc.executeStrategy;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.PreparedStatementSetter;
import nextstep.jdbc.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractExecuteStrategy<T> implements ExecuteStrategy<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractExecuteStrategy.class);

    public T execute(String query, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            return handle(pstmt, pstmtSetter);
        } catch (SQLException e) {
            log.debug(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    public abstract T handle(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException;
}
