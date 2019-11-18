package nextstep.jdbc;

public class ConnectionFailException extends RuntimeException {
    private static final String ERROR_MESSAGE = "데이터베이스 연결에 실패했습니다.";

    public ConnectionFailException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
