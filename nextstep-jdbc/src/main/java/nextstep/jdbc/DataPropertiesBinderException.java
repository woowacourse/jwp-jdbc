package nextstep.jdbc;

public class DataPropertiesBinderException extends RuntimeException {
    public DataPropertiesBinderException(Throwable exception, String message) {
        super(message, exception);
    }
}
