package nextstep.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private final ConnectionManager connectionManager;

    private JdbcTemplate(Builder builder) {
        connectionManager = new ConnectionManager(builder.driver, builder.url,
                builder.userName, builder.password);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void executeUpdate(String sql, Object... args) {
        try (PreparedStatement psmt = prepare(sql)) {
            setParams(psmt, args);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private PreparedStatement prepare(String sql) throws SQLException {
        return this.connectionManager.getConnection().prepareStatement(sql);
    }

    private void setParams(PreparedStatement psmt, Object... args) {
        for (int i = 0; i < args.length; i++) {
            setParamValues(psmt, i, args[i]);
        }
    }

    private void setParamValues(PreparedStatement psmt, int parameterIndex, Object arg) {
        try {
            // PreparedStatement 의 parameter index 는 1부터 시작한다.
            psmt.setObject(parameterIndex + 1, arg);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> Optional<T> queryForObject(String sql, ResultSetMappingStrategy<T> strategy, Object... args) {
        List<T> results = query(sql, strategy, args);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    public <T> List<T> query(String sql, ResultSetMappingStrategy<T> strategy, Object... args) {
        try (PreparedStatement psmt = prepare(sql)) {
            setParams(psmt, args);
            ResultSet rs = psmt.executeQuery();
            return mapResultSet(strategy, rs);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> List<T> mapResultSet(ResultSetMappingStrategy<T> strategy, ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(strategy.map(rs));
        }
        return result;
    }

    public static final class Builder {
        private String driver;
        private String url;
        private String userName;
        private String password;

        private Builder() {
        }

        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public JdbcTemplate build() {
            return new JdbcTemplate(this);
        }
    }
}
