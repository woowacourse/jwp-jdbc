package slipp.dao.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException() {
        super("해당 하는 객체를 찾을 수 없습니다.");
    }
}