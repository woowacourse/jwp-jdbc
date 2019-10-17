package nextstep.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface FunctionThrowingSQLException<T, R> {
    R apply(T x) throws SQLException;
}