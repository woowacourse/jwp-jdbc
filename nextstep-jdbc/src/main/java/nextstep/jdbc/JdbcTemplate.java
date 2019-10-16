package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static nextstep.jdbc.ConnectionManager.getConnection;

public class JdbcTemplate {

    public void executeQuery(String sql, List<Object> parameters) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            executePreparedStatement(pstmt, parameters);
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    private void executePreparedStatement(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        int index = 1;
        for (Object parameter : parameters) {
            pstmt.setObject(index, parameter);
            index++;
        }

        pstmt.executeUpdate();
    }
}
