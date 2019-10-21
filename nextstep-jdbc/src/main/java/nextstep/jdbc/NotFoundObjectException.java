package nextstep.jdbc;

public class NotFoundObjectException extends RuntimeException{
    public NotFoundObjectException() {
        super("Requested Object not found!");
    }
}
