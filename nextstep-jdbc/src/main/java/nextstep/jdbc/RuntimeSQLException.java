package nextstep.jdbc;

import java.sql.SQLException;

public class RuntimeSQLException extends RuntimeException {

    public RuntimeSQLException(final SQLException exception) {
        super(exception);
    }
}
