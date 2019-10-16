package nextstep.jdbc;

public class DbAccessException extends RuntimeException {
    public DbAccessException() {}

    public DbAccessException(final String message) {
        super(message);
    }

    public DbAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DbAccessException(final Throwable cause) {
        super(cause);
    }
}
