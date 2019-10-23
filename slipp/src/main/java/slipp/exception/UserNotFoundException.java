package slipp.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Not Found User");
    }
}
