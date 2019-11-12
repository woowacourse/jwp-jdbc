package nextstep.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate implements DBTemplate {
    private static final PreparedStatementSetter preparedStatementSetter = new DefaultSetter();

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(String query, Object... params) {
        cxud(query, params);
    }

    @Override
    public <A> List<A> readAll(RowMapper<A> rowMapper, String query) {
        try (final Connection con = this.dataSource.getConnection();
             final PreparedStatement pstmt = preparedStatementSetter.run(con, query);
             final ResultSet resultSet = pstmt.executeQuery()) {
            return new ArrayList<>() {{
                while (resultSet.next()) {
                    add(rowMapper.apply(resultSet));
                }
            }};
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    @Override
    public <A> Optional<A> read(RowMapper<A> rowMapper, String query, Object... params) {
        try (final Connection con = this.dataSource.getConnection();
             final PreparedStatement pstmt = preparedStatementSetter.run(con, query, params);
             final ResultSet resultSet = pstmt.executeQuery()) {
            return resultSet.next() ? Optional.of(rowMapper.apply(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }

    @Override
    public void update(String query, Object... params) {
        cxud(query, params);
    }

    @Override
    public void delete(String query, Object... params) {
        cxud(query, params);
    }

    @Override
    public void deleteAll(String query) {
        cxud(query);
    }

    @Override
    public void execute(String query, Object... params) {
        cxud(query, params);
    }

    private void cxud(String query, Object... params) {
        try (final Connection con = this.dataSource.getConnection();
             final PreparedStatement pstmt = preparedStatementSetter.run(con, query, params)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new QueryFailedException(e);
        }
    }
}