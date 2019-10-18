package nextstep.jdbc.template;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.JdbcMapper;
import nextstep.jdbc.query.PreparedStatementBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private final Connection con;

    public JdbcTemplate(Connection con) {
        this.con = con;
    }

    public void updateQuery(PreparedStatementBuilder mapper) {
        try (PreparedStatement pstmt = mapper.create(con)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    public <T> T executeQuery(PreparedStatementBuilder preparedStatementBuilder, JdbcMapper<T> mapper) {
        try (PreparedStatement pstmt = preparedStatementBuilder.create(con);
             ResultSet rs = pstmt.executeQuery()) {

            return mapper.mapped(rs);
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }
}
