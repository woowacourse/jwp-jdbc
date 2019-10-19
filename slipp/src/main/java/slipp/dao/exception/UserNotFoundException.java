package slipp.dao.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
