package slipp.dao.exception;

public class SetValueException extends RuntimeException {

    public SetValueException() {
        super("값을 할당할 수 없습니다.");
    }
}
