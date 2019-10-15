package slipp.dao.exception;

public class ResultMappingException extends RuntimeException {

    public ResultMappingException() {
        super("해당하는 결과가 존재하지 않습니다.");
    }
}
