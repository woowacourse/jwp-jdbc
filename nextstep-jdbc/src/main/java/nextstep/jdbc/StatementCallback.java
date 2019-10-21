package nextstep.jdbc;


import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementCallback<T> {
    T action(PreparedStatement preparedStatement) throws SQLException;
}
