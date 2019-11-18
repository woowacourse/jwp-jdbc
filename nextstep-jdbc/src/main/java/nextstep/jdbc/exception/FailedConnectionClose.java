package nextstep.jdbc.exception;

public class FailedConnectionClose extends RuntimeException {
    private static final String MESSAGE = "DB 연결을 종료할 수 없습니다.";

    public FailedConnectionClose() {
        super(MESSAGE);
    }
}
