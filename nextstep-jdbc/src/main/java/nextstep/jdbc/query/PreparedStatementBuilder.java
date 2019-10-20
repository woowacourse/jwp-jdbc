package nextstep.jdbc.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementBuilder {
    void build(PreparedStatement pstmt) throws SQLException;
}
