package nextstep.jdbc.exception;

public class NoResultException extends RuntimeException {

    public NoResultException() {
        super("쿼리 결과가 없습니다.");
    }
}
