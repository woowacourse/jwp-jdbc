package nextstep.jdbc.exception;

public class SQLSelectException extends RuntimeException {
    private static final String MESSAGE = "데이터베이스를 조회 할 수 없습니다.";

    public SQLSelectException() {
        super(MESSAGE);
    }
}
