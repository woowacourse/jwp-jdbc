package nextstep.jdbc.exception;

public class DataBaseException extends RuntimeException {
    private static final String MESSAGE = "데이터베이스 오류 입니다.";

    public DataBaseException() {
        super(MESSAGE);
    }
}
