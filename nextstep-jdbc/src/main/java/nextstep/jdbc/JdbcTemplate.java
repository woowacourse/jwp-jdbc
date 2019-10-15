package nextstep.jdbc;

import java.sql.PreparedStatement;

public interface JdbcTemplate {
    void setValuesForInsert(Object object, PreparedStatement pstmt);

}