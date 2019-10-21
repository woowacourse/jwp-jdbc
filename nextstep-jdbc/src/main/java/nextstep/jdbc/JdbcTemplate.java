package nextstep.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTemplate implements AutoCloseable {
    private final Connection connection;

    public JdbcTemplate(Connection connection) {
        this.connection = connection;
    }

    public void executeQuery(String sql, List<Object> parameters) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            executePreparedStatement(pstmt, parameters);
        } catch (SQLException e) {
            throw new DatabaseException();
        }
    }

    public <T> T executeQueryThatHasResultSet(String sql, List<Object> parameters, ResultMapper<T> resultMapper) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
