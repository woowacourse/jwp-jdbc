package nextstep.jdbc;

public class DataAccessException extends RuntimeException {
    public DataAccessException() {
        super("DataAccessException occurred!");
    }
}
