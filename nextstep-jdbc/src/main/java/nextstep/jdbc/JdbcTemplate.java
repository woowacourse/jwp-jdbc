package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(String query, Object... params) {
        cxud(query, params);
    }

    public <T> T select(FunctionThrowingSQLException<ResultSet, T> rowMapper, String query, Object... params) {
        try (final Connection con = this.dataSource.getConnection();
             final PreparedStatement pstmt = prepareStatement(con, query, params);
             final ResultSet resultSet = pstmt.executeQuery()) {
            return rowMapper.apply(resultSet);
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    public <T> T selectAll(FunctionThrowingSQLException<ResultSet, T> rowMapper, String query) {
        return select(rowMapper, query);
    }

    public void update(String query, Object... params) {
        cxud(query, params);
    }

    public void delete(String query, Object... params) {
        cxud(query, params);
    }

    public void deleteAll(String query) {
        cxud(query);
    }

    private void cxud(String query, Object... params) {
        try (final Connection con = dataSource.getConnection();
             final PreparedStatement pstmt = prepareStatement(con, query, params)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    private PreparedStatement prepareStatement(
            Connection con, String query, Object... params
    ) throws SQLException {
        final PreparedStatement pstmt = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt;
    }
}