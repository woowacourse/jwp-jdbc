package slipp.exception;

public class DataBasePropertyReadFailException extends RuntimeException {
    public static final String DATABASE_PROPERTY_READ_FAIL_MESSAGE = "DB 설정파일을 읽어올 수 없습니다.";

    public DataBasePropertyReadFailException() {
        super(DATABASE_PROPERTY_READ_FAIL_MESSAGE);
    }

    public DataBasePropertyReadFailException(String message) {
        super(message);
    }
}
