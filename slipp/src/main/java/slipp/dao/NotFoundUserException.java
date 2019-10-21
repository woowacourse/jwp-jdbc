package slipp.dao;

public class NotFoundUserException extends RuntimeException {
    private static final String ERROR_MESSAGE = "존재하지 않는 유저입니다.";

    public NotFoundUserException() {
        super(ERROR_MESSAGE);
    }
}
