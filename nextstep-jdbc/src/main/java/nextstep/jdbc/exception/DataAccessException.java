package nextstep.jdbc.exception;

public class DataAccessException extends RuntimeException {
    private static final String MESSAGE = "Process SQL Error :";

    public DataAccessException(Exception e) {
        super(MESSAGE + e.getMessage(), e);
    }
}
