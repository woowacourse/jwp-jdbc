package nextstep.jdbc.template;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.Mapper;
import nextstep.jdbc.mapper.TableMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public <T> T execute(String sql, Mapper<T> mapper, Object... parameters) {
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            setParameters(pstmt, Arrays.asList(parameters));
            ResultSet resultSet = pstmt.executeQuery();
            return mapper.createRow(resultSet);
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    private void setParameters(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            pstmt.setObject(i + 1, parameters.get(i));
        }
    }
}
