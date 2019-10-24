package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    public <T> List<T> selectTemplate(String sql, RowMapper<T> rowMapper, Object... params) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), params);
             ResultSet rs = pstmt.executeQuery()) {
            return getRows(rowMapper, rs);
        } catch (Exception e) {
            throw new SelectQueryFailException(e);
        }
    }

    public <T> List<T> selectTemplate(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), pstmtSetter);
             ResultSet rs = pstmt.executeQuery()) {
            return getRows(rowMapper, rs);
        } catch (Exception e) {
            throw new SelectQueryFailException(e);
        }
    }

    public <T> T selectObjectTemplate(String sql, PreparedStatementSetter pstmtSetter, RowMapper<T> rowMapper) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), pstmtSetter);
             ResultSet rs = pstmt.executeQuery()) {
            return getObject(rowMapper, rs);
        } catch (Exception e) {
            throw new SelectQueryFailException(e);
        }
    }

    public <T> T selectObjectTemplate(String sql, RowMapper<T> rowMapper, Object... params) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), params);
             ResultSet rs = pstmt.executeQuery()) {
            return getObject(rowMapper, rs);
        } catch (Exception e) {
            throw new SelectQueryFailException(e);
        }
    }

    public void updateTemplate(String sql, PreparedStatementSetter pstmtSetter) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmtSetter.setValues(pstmt);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UpdateQueryFailException(e);
        }
    }

    public void updateTemplate(String sql, Object... params) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = setValues(con.prepareStatement(sql), params)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UpdateQueryFailException(e);
        }
    }

    private PreparedStatement setValues(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 1; i <= params.length; i++) {
            pstmt.setObject(i, params[i - 1]);
        }
        return pstmt;
    }

    private PreparedStatement setValues(PreparedStatement pstmt, PreparedStatementSetter pstmtSetter) throws SQLException {
        pstmtSetter.setValues(pstmt);
        return pstmt;
    }

    private <T> List<T> getRows(RowMapper<T> rowMapper, ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rowMapper.mapRow(rs));
        }
        return result;
    }

    private <T> T getObject(RowMapper<T> rowMapper, ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rowMapper.mapRow(rs);
        }
        throw new SelectObjectNotFoundException();
    }
}
