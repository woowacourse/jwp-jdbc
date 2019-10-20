package nextstep.jdbc;

import nextstep.jdbc.exception.JdbcTemplateSqlException;
import nextstep.jdbc.mapper.TableMapper;
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
        try (PreparedStatement pst = con.prepareStatement(query)) {
            statementBuilder.build(pst);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }

    public <T> T executeQuery(String query, PreparedStatementBuilder statementBuilder, TableMapper<T> mapper) {
        try (PreparedStatement pst = con.prepareStatement(query)) {
            statementBuilder.build(pst);
            return mapper.mapped(pst.executeQuery());
        } catch (SQLException e) {
            throw new JdbcTemplateSqlException(e);
        }
    }
}
