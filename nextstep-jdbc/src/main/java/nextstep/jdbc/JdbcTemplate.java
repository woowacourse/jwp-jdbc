package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.JdbcMapper;
import nextstep.jdbc.query.PreparedStatementBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {
    private final Connection con;

    public JdbcTemplate(Connection con) {
        this.con = con;
    }

    public void updateQuery(String query, PreparedStatementBuilder statementBuilder) {
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            statementBuilder.build(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    public <T> T executeQuery(String query, PreparedStatementBuilder statementBuilder, JdbcMapper<T> mapper) {
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            statementBuilder.build(pstmt);
            return mapper.mapped(pstmt.executeQuery());
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }
}
