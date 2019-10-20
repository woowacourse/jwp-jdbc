package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VarargsPreparedStatementSetter implements PreparedStatementSetter {
    private Object[] values;

    VarargsPreparedStatementSetter(Object... values) {
        this.values = values;
    }

    @Override
    public void setValues(PreparedStatement pstmt) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }
    }
}
