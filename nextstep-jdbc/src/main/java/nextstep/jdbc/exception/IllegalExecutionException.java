package nextstep.jdbc.exception;

public class IllegalExecutionException extends RuntimeException {
    public IllegalExecutionException(Exception cause) {
        super(cause);
    }
}
