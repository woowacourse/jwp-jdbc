package nextstep.jdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcQuery {
    PreparedStatement create(Connection con) throws SQLException;
}
