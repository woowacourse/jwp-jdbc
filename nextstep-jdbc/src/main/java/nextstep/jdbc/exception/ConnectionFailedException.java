package nextstep.jdbc.exception;

import java.sql.SQLException;

public class ConnectionFailedException extends RuntimeException {
    public ConnectionFailedException(final SQLException e) {
        super(e);
    }
}
