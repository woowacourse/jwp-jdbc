package nextstep.jdbc;

public class DataAccessException extends RuntimeException {
    public DataAccessException() {
        super("Data Access Exception Occurred!");
    }
}
