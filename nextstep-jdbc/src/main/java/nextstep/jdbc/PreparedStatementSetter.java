package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {
    PreparedStatement run(Connection con, String query, Object... params) throws SQLException;
}