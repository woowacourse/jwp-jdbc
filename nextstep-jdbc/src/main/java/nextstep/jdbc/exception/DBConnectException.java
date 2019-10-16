package nextstep.jdbc.exception;

public class DBConnectException extends RuntimeException {

    public DBConnectException() {
        super("DB Connection 생성이 불가능합니다.");
    }
}
