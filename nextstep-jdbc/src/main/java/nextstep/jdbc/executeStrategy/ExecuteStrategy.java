package nextstep.jdbc.executeStrategy;

import nextstep.jdbc.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ExecuteStrategy<T> {
    T execute(String query, PreparedStatementSetter pstmtSetter);

    T handle(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException;
}
