package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementMapping {
    void adjustTo(PreparedStatement pstmt) throws SQLException;
}
