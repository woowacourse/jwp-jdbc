package nextstep.jdbc.exception;

public class DataSourceException extends RuntimeException {
    public DataSourceException(final Exception e) {
        super(e);
    }
}
