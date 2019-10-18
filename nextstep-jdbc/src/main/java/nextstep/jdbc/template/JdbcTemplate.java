package nextstep.jdbc.template;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.JdbcMapper;
import nextstep.jdbc.query.PreparedStatementBuilder;

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

    public <T> T executeQuery(PreparedStatementBuilder preparedStatementBuilder, JdbcMapper<T> mapper) {
        try (PreparedStatement pstmt = preparedStatementBuilder.create(con);
             ResultSet rs = pstmt.executeQuery()) {

            return mapper.mapped(rs);
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    public void update(String sql, Object... parameters) {
        List<Object> objects = Arrays.asList(parameters);
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (int i = 1; i <= objects.size(); i++) {
                pstmt.setObject(i, objects.get(i - 1));
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }
}
