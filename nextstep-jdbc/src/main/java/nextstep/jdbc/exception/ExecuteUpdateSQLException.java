package nextstep.jdbc.exception;

public class ExecuteUpdateSQLException extends RuntimeException {
    public ExecuteUpdateSQLException() {
        super("쿼리 실행 중 에러가 발생했습니다.");
    }
}
