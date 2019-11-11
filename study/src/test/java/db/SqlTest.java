package db;

import dto.DevType;
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

    private static final String SELECT_HOBBY_AND_RATIO = "select Hobby, (COUNT(Hobby) * 100 / (select COUNT(Hobby) from survey_results_public)) ratio " +
            "from survey_results_public " +
            "group by Hobby";

    private static final String SELECT_DEVTYPE_AND_AVG = "SELECT\n" +
            "    DevType, ROUND(AVG(YEARS), 1) avg\n" +
            "FROM\n" +
            "    (SELECT\n" +
            "         SUBSTRING_INDEX(SUBSTRING_INDEX(YearsCodingProf, ' ', 1), '-', 1) 'YEARS',\n" +
            "         SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType, ';', numbers.n), ';', - 1) DevType\n" +
            "     FROM\n" +
            "         (SELECT 1 n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20) numbers\n" +
            "             INNER JOIN survey_results_public ON CHAR_LENGTH(survey_results_public.DevType) - CHAR_LENGTH(REPLACE(survey_results_public.DevType, ';', '')) >= numbers.n - 1\n" +
            "     WHERE\n" +
            "             YearsCodingProf != 'NA') SPLIT_TABLE\n" +
            "GROUP BY DevType\n" +
            "ORDER BY avg DESC;";

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    
    @BeforeEach
    void setup() throws Exception {
        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream("../slipp/src/main/resources/db.properties")));
        ConnectionManager.init(BasicDataSourceFactory.getDataSource(properties));
    }
    @Test
    void hobby_time() {
        assertTimeout(Duration.ofMillis(2000), () -> {
            jdbcTemplate.query(SELECT_HOBBY_AND_RATIO, rs -> new HobbyDto(rs.getString("Hobby"), rs.getDouble("ratio")));
        });
    }
    @Test
    void hobby_ratio() {
        List<HobbyDto> hobbyDtos = jdbcTemplate.query(SELECT_HOBBY_AND_RATIO, rs -> new HobbyDto(rs.getString("Hobby"), rs.getDouble("ratio")));
        hobbyDtos.forEach(hobbyDto -> logger.info("hobby : {}, ratio : {}", hobbyDto.getHobby(), hobbyDto.getRatio()));
    }

    @Test
    void devType_time() {
        assertTimeout(Duration.ofMillis(4000), () -> {
            jdbcTemplate.query(SELECT_DEVTYPE_AND_AVG, rs -> new DevType(rs.getString("DevType"), rs.getDouble("avg")));
        });
    }

    @Test
    void devType_avg() {
        List<DevType> devTypes = jdbcTemplate.query(SELECT_DEVTYPE_AND_AVG, rs -> new DevType(rs.getString("DevType"), rs.getDouble("avg")));
        devTypes.forEach(devType -> logger.info("devType : {}, avg : {}", devType.getDevType(), devType.getAvg()));
    }
}