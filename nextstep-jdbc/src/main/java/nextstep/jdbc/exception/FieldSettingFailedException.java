package nextstep.jdbc.exception;

public class FieldSettingFailedException extends RuntimeException {
    public FieldSettingFailedException() {
        super("Field 설정에 실패했습니다.");
    }
}
