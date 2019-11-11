package nextstep.jdbc;

public class PropertiesLoadException extends RuntimeException {
    public PropertiesLoadException(Exception e) {
        super("Properties Load Exception occurred!!", e);
    }
}
