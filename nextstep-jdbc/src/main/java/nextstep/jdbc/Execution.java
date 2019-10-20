package nextstep.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface Execution<T, R> {
    R execute(T target) throws SQLException;
}
