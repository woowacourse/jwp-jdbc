package nextstep.jdbc.exception;

import java.sql.SQLException;

public class InsertionFailedException extends RuntimeException {
    public InsertionFailedException(final SQLException e) {
        super(e);
    }
}
