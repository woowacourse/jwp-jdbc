package slipp.dao;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found exception occurred");
    }
}
