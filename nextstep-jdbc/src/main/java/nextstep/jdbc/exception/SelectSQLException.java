package nextstep.jdbc.exception;

public class SelectSQLException extends RuntimeException {

    public SelectSQLException() {
        super("Select 쿼리 중 에러 발생");
    }
}
