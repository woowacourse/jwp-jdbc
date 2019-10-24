package nextstep.jdbc;

public class SelectQueryFailException extends RuntimeException {
    private static final String ERROR_MSG = "Select 쿼리를 수행하는 데 실패했습니다.";

    public SelectQueryFailException() {
        super(ERROR_MSG);
    }

    public SelectQueryFailException(Throwable cause) {
        super(ERROR_MSG, cause);
    }
}
