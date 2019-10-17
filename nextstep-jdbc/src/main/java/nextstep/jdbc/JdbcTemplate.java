package nextstep.jdbc;

import nextstep.jdbc.exception.IllegalConnectionException;
import nextstep.jdbc.exception.IllegalExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void execute(String sql, Object... values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setValues(preparedStatement, values);

            preparedStatement.executeUpdate();
        } catch (SQLException | IllegalConnectionException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(e);
        }
    }

    public <T> T executeQuery(String sql, ResultSetProcessor<T> resultSetProcessor, Object... values) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setValues(preparedStatement, values);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSetProcessor.process(resultSet);
        } catch (SQLException | IllegalConnectionException e) {
            log.error("execution error : ", e);
            throw new IllegalExecutionException(e);
        }
    }

    private void setValues(PreparedStatement preparedStatement, Object[] values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
    }
}
