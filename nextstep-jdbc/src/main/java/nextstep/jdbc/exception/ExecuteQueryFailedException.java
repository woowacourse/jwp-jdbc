package nextstep.jdbc.exception;

import java.sql.SQLException;

public class ExecuteQueryFailedException extends RuntimeException {
    public ExecuteQueryFailedException(final SQLException e) {
        super(e);
    }
}
