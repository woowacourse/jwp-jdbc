package slipp.controller.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super(String.format("[userId: %s] 해당 id 를 사용하는 사용자를 찾을 수 없습니다.", userId));
    }

    public static UserNotFoundException ofUserId(String userId) {
        return new UserNotFoundException(userId);
    }
}
