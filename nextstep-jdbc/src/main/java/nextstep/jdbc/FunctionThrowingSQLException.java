package nextstep.jdbc;

import java.sql.SQLException;

@FunctionalInterface
public interface FunctionThrowingSQLException<A, B> {
    B apply(A x) throws SQLException;
}