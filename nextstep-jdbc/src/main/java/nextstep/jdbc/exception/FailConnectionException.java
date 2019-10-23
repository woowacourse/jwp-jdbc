package nextstep.jdbc.exception;

public class FailConnectionException extends RuntimeException {
    private static final String ERROR_MESSAGE = "데이터베이스를 연결하는데 실패하였습니다.";

    public FailConnectionException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
