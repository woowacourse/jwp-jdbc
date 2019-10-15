package nextstep.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ExecuteQuery<T> {
     T execute(Connection con) throws SQLException;
}
