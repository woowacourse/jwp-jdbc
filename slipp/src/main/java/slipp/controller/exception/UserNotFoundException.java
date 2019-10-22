package slipp.controller.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.");
    }
}
