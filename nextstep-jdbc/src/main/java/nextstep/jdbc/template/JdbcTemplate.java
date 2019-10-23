package nextstep.jdbc.template;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.RowMapper;
import nextstep.jdbc.mapper.TableMapper;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcTemplate {
    private final Connection con;

    public JdbcTemplate(Connection con) {
        this.con = con;
    }

    public void update(String sql, Object... parameters) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, Arrays.asList(parameters));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    private void setParameters(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            pstmt.setObject(i + 1, parameters.get(i));
        }
    }

    @Nullable
    public <T> T executeForObject(String sql, RowMapper<T> mapper, Object... parameters) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, Arrays.asList(parameters));
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return mapper.create(resultSet);
            }
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
        return null;
    }

    public <T> List<T> executeForList(String sql, RowMapper<T> mapper, Object... parameters) {
        List<T> results = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, Arrays.asList(parameters));
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                results.add(mapper.create(resultSet));
            }
            return results;
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }
}
