package nextstep.jdbc.exception;

public class SQLUpdateException extends RuntimeException {
    private static final String MESSAGE = "데이터베이스를 수정할 수 없습니다.";

    public SQLUpdateException() {
        super(MESSAGE);
    }
}