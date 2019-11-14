package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;
import sql.dto.DevTypeDto;
import sql.dto.HobbyDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {

    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.dataSource = ConnectionManager.getDataSource();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void hobbyPercent() throws SQLException {
        String sql = "SELECT Hobby, round(count(*) * 100 / (SELECT count(*) FROM survey_results_public), 1) as percent\n" +
            "FROM survey_results_public\n" +
            "GROUP BY Hobby;";
        RowMapper rowMapper = rs ->
            new HobbyDto(rs.getString("Hobby"), rs.getString("percent"));

        Connection connection = dataSource.getConnection();
        assertTimeout(Duration.ofMillis(100), () -> {
            jdbcTemplate.query(sql, connection, rowMapper);
        });
    }

    @Test
    void devTypeYears() throws SQLException {
        String sql = "SELECT dev, round(avg(years), 1) as years\n" +
            "FROM result\n" +
            "WHERE years <> 'NA' && dev <> 'NA'\n" +
            "GROUP BY dev;";
        RowMapper rowMapper = rs ->
            new DevTypeDto(rs.getString("dev"), rs.getString("years"));

        Connection connection = dataSource.getConnection();
        assertTimeout(Duration.ofMillis(310), () -> {
            jdbcTemplate.query(sql, connection, rowMapper);
        });
    }
}