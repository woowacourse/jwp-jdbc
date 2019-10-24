package nextstep.jdbc;

import nextstep.jdbc.exception.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementCallback<T> {
    T doInPreparedStatement(final PreparedStatement pstmt) throws SQLException, DataAccessException;
}
