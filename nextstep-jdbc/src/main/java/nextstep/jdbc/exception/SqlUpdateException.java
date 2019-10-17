package nextstep.jdbc.exception;

public class SqlUpdateException extends RuntimeException {
    private static final String SQL_UPDATE_MESSAGE = "쿼리 업데이트를 실행할 수 없습니다.";

    public SqlUpdateException() {
        super(SQL_UPDATE_MESSAGE);
    }
}
