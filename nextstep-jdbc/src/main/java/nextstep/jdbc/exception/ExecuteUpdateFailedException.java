package nextstep.jdbc.exception;

import java.sql.SQLException;

public class ExecuteUpdateFailedException extends RuntimeException {
    public ExecuteUpdateFailedException(final SQLException e) {
        super(e);
    }
}
