package nextstep.jdbc;

import slipp.domain.User;
import slipp.support.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class InsertJdbcTemplate {
    public void insert(User user) throws SQLException {
        String sql = createQueryForInsert();

        try (Connection con = ConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            setValuesForInsert(user, pstmt);
            pstmt.executeUpdate();
        }
    }


    public abstract String createQueryForInsert();

    public abstract void setValuesForInsert(User user, PreparedStatement pstmt) throws SQLException;
}
