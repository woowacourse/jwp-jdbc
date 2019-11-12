package slipp.support.db;

public class PropertyLoadFailedException extends RuntimeException {
    public PropertyLoadFailedException() {
        super("Property load에 실패했습니다.");
    }
}