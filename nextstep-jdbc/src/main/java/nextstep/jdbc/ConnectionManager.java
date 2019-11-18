package nextstep.jdbc;

import nextstep.jdbc.exception.FailedConnection;
import nextstep.jdbc.exception.FailedConnectionClose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String TAG = "ConnectionManager";

    public static Connection initConnection(final DataSource dataSource) {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("{}.initConnection >> {}", TAG, e);
            throw new FailedConnection();
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("{}.closeConnection >> {}", TAG, e);
            throw new FailedConnectionClose();
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            closeConnection(connection);
        } catch (SQLException e) {
            logger.error("{}.closeConnection >> {}", TAG, e);
            throw new FailedConnectionClose();
        }
    }
}
