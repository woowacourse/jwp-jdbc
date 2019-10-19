package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void executeQuery(String sql, List<Object> parameters) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            executePreparedStatement(pstmt, parameters);
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public <T> T executeQueryThatHasResultSet(String sql, List<Object> parameters, ResultMapper<T> resultMapper) {
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            return resultMapper.map(getResultSet(pstmt, parameters));
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    private ResultSet getResultSet(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        int index = 1;
        for (Object parameter : parameters) {
            pstmt.setObject(index, parameter);
            index++;
        }

        return pstmt.executeQuery();
    }

    private void executePreparedStatement(PreparedStatement pstmt, List<Object> parameters) throws SQLException {
        int index = 1;
        for (Object parameter : parameters) {
            pstmt.setObject(index, parameter);
            index++;
        }

        pstmt.executeUpdate();
    }
}
