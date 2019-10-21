package nextstep.jdbc;

public class JdbcTemplateException extends RuntimeException {
    public JdbcTemplateException() {
        super();
    }

    public JdbcTemplateException(String message) {
        super(message);
    }

    public JdbcTemplateException(Throwable cause) {
        super(cause);
    }
}
