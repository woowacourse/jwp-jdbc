package slipp.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("Not found user exception!");
    }
}
