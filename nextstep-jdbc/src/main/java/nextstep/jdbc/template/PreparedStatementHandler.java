package nextstep.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementHandler<T> {
    T handle(PreparedStatement pstmt) throws SQLException;
}
