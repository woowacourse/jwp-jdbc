package nextstep.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface UpdateQuery {
    void update(Connection con) throws SQLException;
}
