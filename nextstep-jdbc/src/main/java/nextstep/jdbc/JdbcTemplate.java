package nextstep.jdbc;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class JdbcTemplate {
    public void insert() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = createSql();
            pstmt = con.prepareStatement(sql);
            setParameters(pstmt);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    protected abstract String createSql();

    protected abstract void setParameters(PreparedStatement preparedStatement) throws SQLException;
}
