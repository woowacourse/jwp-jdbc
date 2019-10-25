package slipp.support.db.exception;

public class PropertiesIOException extends RuntimeException {
    public PropertiesIOException() {
        super();
    }

    public PropertiesIOException(String message) {
        super(message);
    }

    public PropertiesIOException(Throwable cause) {
        super(cause);
    }
}
