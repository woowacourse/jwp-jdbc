package nextstep.jdbc.exception;

public class SelectQueryException extends RuntimeException {
    private static final String SINGLE_QUERY_MESSAGE = "Select 쿼리를 실행할 수 없습니다.";

    public SelectQueryException() {
        super(SINGLE_QUERY_MESSAGE);
    }
}
