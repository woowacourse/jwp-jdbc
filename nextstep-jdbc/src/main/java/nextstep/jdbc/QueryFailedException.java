package nextstep.jdbc;

public class QueryFailedException extends RuntimeException {
    public QueryFailedException(Throwable cause) {
        super(cause);
    }
}