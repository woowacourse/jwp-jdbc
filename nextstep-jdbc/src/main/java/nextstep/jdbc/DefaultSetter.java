package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultSetter implements PreparedStatementSetter {
    @Override
    public PreparedStatement run(Connection con, String query, Object... params) throws SQLException {
        final PreparedStatement pstmt = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt;
    }
}