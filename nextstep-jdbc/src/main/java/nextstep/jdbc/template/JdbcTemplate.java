package nextstep.jdbc.template;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.JdbcMapper;
import nextstep.jdbc.query.JdbcQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate implements DbcTemplate {
    private final Connection con;

    public JdbcTemplate(Connection con) {
        this.con = con;
    }

    public void updateQuery(JdbcQuery query) {
        try (PreparedStatement pstmt = query.execute(con)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    public <T> T executeQuery(JdbcQuery query, JdbcMapper<T> mapper) {
        try (PreparedStatement pstmt = query.execute(con);
             ResultSet rs = pstmt.executeQuery()) {

            return mapper.mapped(rs);
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }
}
