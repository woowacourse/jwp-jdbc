package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    public void executeUpdate(String sql, Object... queryParams) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            setQueryParams(pstmt, queryParams);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public <T> List<T> selectAll(String sql, RowMapper rowMapper, Object... queryParams) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            setQueryParams(pstmt, queryParams);
            ResultSet resultSet = pstmt.executeQuery();

            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add((T) rowMapper.mapRow(resultSet));
            }
            return results;

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public <T> T selectObject(String sql, RowMapper rowMapper, Object... queryParams) {
        Optional<T> result = Optional.empty();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            setQueryParams(pstmt, queryParams);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                result = Optional.of((T) rowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return result.orElseThrow(() -> new DataAccessException("결과를 찾을 수 없습니다."));
    }

    private void setQueryParams(PreparedStatement pstmt, Object[] queryParams) throws SQLException {
        for (int i = 1; i <= queryParams.length; i++) {
            pstmt.setObject(i, queryParams[i - 1]);
        }
    }
}
