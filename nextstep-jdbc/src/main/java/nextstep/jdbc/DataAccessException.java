package nextstep.jdbc;

public class DataAccessException extends RuntimeException {

    private static final String MESSAGE = "Error: process sql";

    public DataAccessException() {
        super(MESSAGE);
    }
}
