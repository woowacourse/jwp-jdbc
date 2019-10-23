package nextstep.jdbc.support;

public class DataBaseInitializerException extends RuntimeException {
    public DataBaseInitializerException(final String message, final Exception e) {
        super(message, e);
    }
}
