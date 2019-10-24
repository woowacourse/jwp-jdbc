package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PrepareStatementSetter {
    void setParameters(PreparedStatement preparedStatement) throws SQLException;
}
