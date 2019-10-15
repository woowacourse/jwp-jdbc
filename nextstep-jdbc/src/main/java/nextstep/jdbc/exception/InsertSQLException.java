package nextstep.jdbc.exception;

public class InsertSQLException extends RuntimeException {
    public InsertSQLException() {
        super("데이터 삽입이 불가능합니다.");
    }
}
