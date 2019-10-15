package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface UpdateQuery {
    PreparedStatement update(Connection con) throws SQLException;
}
