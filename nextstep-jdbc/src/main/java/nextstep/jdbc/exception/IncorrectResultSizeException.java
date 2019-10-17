package nextstep.jdbc.exception;

public class IncorrectResultSizeException extends RuntimeException {
    private static final String MESSAGE = "결과의 개수가 올바르지 않습니다.";

    public IncorrectResultSizeException() {
        super(MESSAGE);
    }
}
