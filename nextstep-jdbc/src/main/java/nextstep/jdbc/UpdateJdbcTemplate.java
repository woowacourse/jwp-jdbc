package nextstep.jdbc;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class UpdateJdbcTemplate {
    public void update(User user) throws SQLException {
        String sql = createQueryForUpdate();
        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesForUpdate(user, pstmt);
            pstmt.executeUpdate();
        }
    }

    public abstract String createQueryForUpdate();

    public abstract void setValuesForUpdate(User user, PreparedStatement pstmt) throws SQLException;
}
