package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private ConnectionManager connectionManager;

    public JdbcTemplate(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void executeUpdate(String sql, Object... queryParams) {
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            setQueryParams(pstmt, queryParams);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public <T> T executeQuery(String sql, RowMapper rowMapper, Object... queryParams) {
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            setQueryParams(pstmt, queryParams);
            ResultSet resultSet = pstmt.executeQuery();
            return (T) rowMapper.mapRow(resultSet);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void setQueryParams(PreparedStatement pstmt, Object[] queryParams) throws SQLException {
        for (int i = 1; i <= queryParams.length; i++) {
            pstmt.setObject(i, queryParams[i - 1]);
        }
    }
}
