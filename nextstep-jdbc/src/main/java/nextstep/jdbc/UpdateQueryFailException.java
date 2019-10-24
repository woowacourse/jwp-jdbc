package nextstep.jdbc;

public class UpdateQueryFailException extends RuntimeException {
    private static final String ERROR_MSG = "Update 쿼리를 수행하는 데 실패했습니다.";

    public UpdateQueryFailException() {
        super(ERROR_MSG);
    }

    public UpdateQueryFailException(Throwable cause) {
        super(ERROR_MSG, cause);
    }
}
