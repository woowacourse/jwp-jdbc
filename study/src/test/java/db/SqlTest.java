package db;

import dto.HobbyDto;
import nextstep.jdbc.db.BasicDataSourceFactory;
import nextstep.jdbc.db.ConnectionManager;
import nextstep.jdbc.template.JdbcTemplate;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {
    private static final Logger logger = LoggerFactory.getLogger(SqlTest.class);
    @BeforeEach
    void setup() throws Exception {
        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream("../slipp/src/main/resources/db.properties")));
        ConnectionManager.init(BasicDataSourceFactory.getDataSource(properties));
    }
    @Test
    void hobby_time() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "select Hobby, (COUNT(Hobby) * 100 / (select COUNT(Hobby) from survey_results_public)) ratio " +
                "from survey_results_public " +
                "group by Hobby";
        assertTimeout(Duration.ofMillis(1000), () -> {
            jdbcTemplate.query(sql, rs -> new HobbyDto(rs.getString("Hobby"), rs.getDouble("ratio")));
        });
    }
    @Test
    void hobby_ratio() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sql = "select Hobby, (COUNT(Hobby) * 100 / (select COUNT(Hobby) from survey_results_public)) ratio " +
                "from survey_results_public " +
                "group by Hobby";
        List<HobbyDto> hobbyDtos = jdbcTemplate.query(sql, rs -> new HobbyDto(rs.getString("Hobby"), rs.getDouble("ratio")));
        hobbyDtos.forEach(hobbyDto -> logger.info("hobby : {}, ratio : {}", hobbyDto.getHobby(), hobbyDto.getRatio()));
    }
}