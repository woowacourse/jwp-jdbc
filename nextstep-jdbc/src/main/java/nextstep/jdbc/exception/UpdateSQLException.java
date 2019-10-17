package nextstep.jdbc.exception;

public class UpdateSQLException extends RuntimeException {

    public UpdateSQLException() {
        super("데이터 수정이 불가능합니다.");
    }
}
