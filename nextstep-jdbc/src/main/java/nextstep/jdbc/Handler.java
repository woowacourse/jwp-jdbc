package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface Handler<T> {
    T handle(PreparedStatement pstmt) throws SQLException;
}
