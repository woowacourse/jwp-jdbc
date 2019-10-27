package nextstep.jdbc.exception;

public class FailedConnection extends RuntimeException {
    private static final String MESSAGE = "DB 연결을 할 수 없습니다.";

    public FailedConnection() {
        super(MESSAGE);
    }
}
