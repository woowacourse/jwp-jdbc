package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private final Object[] params;

    public ArgumentPreparedStatementSetter(final Object... params) {
        this.params = params;
    }

    @Override
    public void setValues(final PreparedStatement pstmt) throws SQLException {
        for (int i = 0; i < this.params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
}
