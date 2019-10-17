package nextstep.jdbc.exception;

public class NotOnlyResultException extends RuntimeException {

    public NotOnlyResultException() {
        super("결과가 하나 이상입니다.");
    }
}
