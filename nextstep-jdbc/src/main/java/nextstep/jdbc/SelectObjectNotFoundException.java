package nextstep.jdbc;

public class SelectObjectNotFoundException extends RuntimeException {

    private static final String ERROR_MSG = "객체를 찾지 못했습니다.";

    public SelectObjectNotFoundException() {
        super(ERROR_MSG);
    }

    public SelectObjectNotFoundException(Throwable cause) {
        super(ERROR_MSG, cause);
    }
}
