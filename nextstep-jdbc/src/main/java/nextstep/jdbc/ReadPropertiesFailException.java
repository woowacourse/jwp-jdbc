package nextstep.jdbc;

public class ReadPropertiesFailException extends RuntimeException {
    public ReadPropertiesFailException() {
        super("프로퍼티 파일을 파싱하는 데 실패했습니다.");
    }
}