package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ExecuteCallback<T> {
    T result(PreparedStatement preparedStatement) throws SQLException;
}
