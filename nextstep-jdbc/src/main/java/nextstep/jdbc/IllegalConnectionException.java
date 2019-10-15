package nextstep.jdbc;

public class IllegalConnectionException extends RuntimeException {
    public IllegalConnectionException(String message) {
        super(message);
    }
}
