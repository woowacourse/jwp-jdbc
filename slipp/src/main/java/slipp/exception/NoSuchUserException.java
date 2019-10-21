package slipp.exception;

public class NoSuchUserException extends RuntimeException{
    public NoSuchUserException() {
        super("해당 사용자가 존재하지 않습니다.");
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }
}
