package nextstep.jdbc;

public class DataSourceFailException extends RuntimeException {
    public DataSourceFailException() {
        super("dbcp를 초기화하는 데 실패했습니다.");
    }
}
