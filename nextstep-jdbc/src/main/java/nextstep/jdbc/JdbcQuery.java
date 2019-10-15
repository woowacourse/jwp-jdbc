package nextstep.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface JdbcQuery {
    void service(Connection con) throws SQLException;
}
