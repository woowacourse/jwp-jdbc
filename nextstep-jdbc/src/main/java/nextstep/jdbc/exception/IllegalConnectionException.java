package nextstep.jdbc.exception;

public class IllegalConnectionException extends RuntimeException {
    public IllegalConnectionException(String message) {
        super(message);
    }
}
