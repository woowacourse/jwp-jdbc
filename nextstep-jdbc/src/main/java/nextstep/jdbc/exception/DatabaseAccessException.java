package nextstep.jdbc.exception;

public class DatabaseAccessException extends RuntimeException {
    public DatabaseAccessException(Throwable cause) {
        super(cause);
    }
}
