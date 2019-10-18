package nextstep.jdbc.exception;

public class ExecuteUpdateFailedException extends RuntimeException {
    public ExecuteUpdateFailedException(final Exception e) {
        super(e);
    }
}
