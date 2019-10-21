package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class JdbcProxyTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcProxyTemplate.class);

    private ConnectionManager connectionManager;
    private JdbcTemplate template;

    public JdbcProxyTemplate(DBConnection connection) {
        connectionManager = new ConnectionManager(connection);
        template = new JdbcTemplate();
    }

    public void execute(String sql, Object... objects) {
        Connection con = connectionManager.getConnection();
        try {
            template.execute(con, sql, objects);
        } catch (SQLException e) {
            rollback(con);
            log.debug("executeUpdate Exception! {}, {}", e.getMessage(), e.getCause());
            throw new DataAccessException();
        } finally {
            closeConnection(con);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rm) {
        try (Connection con = connectionManager.getConnection()) {
            return template.query(con, sql, rm);
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rm, Object... objects) {
        try (Connection con = connectionManager.getConnection()) {
            return template.queryForObject(con, sql, rm, objects).orElseThrow(NotFoundObjectException::new);
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }

    private void closeConnection(Connection con) {
        if (Objects.nonNull(con)) {
            try {
                con.close();
            } catch (SQLException e) {
                log.debug(e.getMessage(), e.getCause());
            }
        }
    }

    private void rollback(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            log.debug(e.getMessage(), e.getCause());
            throw new DataAccessException();
        }
    }
}
