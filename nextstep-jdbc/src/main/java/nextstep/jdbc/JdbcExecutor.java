package nextstep.jdbc;

import nextstep.jdbc.exception.DatabaseAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcExecutor {
    private static final Logger logger = LoggerFactory.getLogger(JdbcExecutor.class);

    public static <T> T execute(String sql, Handler<T> handler) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            return handler.handle(pstmt);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAccessException(e);
        }
    }
}
