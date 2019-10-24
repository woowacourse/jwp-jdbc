package slipp.support.db.exception;

public class NotFoundProperties extends RuntimeException {
    private static final String MESSAGE = "설정 파일을 찾을 수 없습니다.";

    public NotFoundProperties() {
        super(MESSAGE);
    }
}
