package nextstep.jdbc;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface PreparedStatementMapping {
    void adjustTo(PreparedStatement pstmt);
}
