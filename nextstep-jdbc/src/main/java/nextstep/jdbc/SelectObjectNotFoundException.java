package nextstep.jdbc;

public class SelectObjectNotFoundException extends RuntimeException {
    public SelectObjectNotFoundException() {
        super("객체를 찾지 못했습니다.");
    }
}
