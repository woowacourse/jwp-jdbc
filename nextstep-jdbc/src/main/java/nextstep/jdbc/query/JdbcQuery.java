package nextstep.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcQuery {
    PreparedStatement execute(Connection con) throws SQLException;
}
