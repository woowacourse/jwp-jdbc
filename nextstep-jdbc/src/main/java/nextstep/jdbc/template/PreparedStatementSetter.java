package nextstep.jdbc.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Deprecated
public interface PreparedStatementSetter {
    void setValues(PreparedStatement pstmt) throws SQLException;
}
