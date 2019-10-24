package slipp.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String USER_NOT_FOUND_MESSAGE = "유저를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(USER_NOT_FOUND_MESSAGE);
    }
}
