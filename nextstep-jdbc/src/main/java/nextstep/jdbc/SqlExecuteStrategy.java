package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlExecuteStrategy {
    void setValues(PreparedStatement preparedStatement) throws SQLException;
}