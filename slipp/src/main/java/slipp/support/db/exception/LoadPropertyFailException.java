package slipp.support.db.exception;

public class LoadPropertyFailException extends RuntimeException {

    private static final String LOAD_PROPERTY_FAIL_MESSAGE = "fail to load property!";

    public LoadPropertyFailException() {
        super(LOAD_PROPERTY_FAIL_MESSAGE);
    }
}
