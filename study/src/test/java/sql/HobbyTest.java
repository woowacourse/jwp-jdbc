package sql;

import nextstep.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class HobbyTest {
    private static final Logger log = LoggerFactory.getLogger(HobbyTest.class);

    static DataSource dataSource;

    @BeforeAll
    static void before() {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/db.properties");

            properties.load(new BufferedInputStream(fileInputStream));
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(properties.getProperty("jdbc.driverClass"));
            ds.setUrl(properties.getProperty("jdbc.url"));
            ds.setUsername(properties.getProperty("jdbc.username"));
            ds.setPassword(properties.getProperty("jdbc.password"));
            dataSource = ds;
        } catch (IOException e) {
            log.error("DB 설정파일을 찾을 수 없습니다.");
        }
    }

    @Test
    void assertTest() {
        String query = "select * from survey_results_public where Respondent = ?";

        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)) {
            Result result = jdbcTemplate.select(this::resultMapper, query, 1);

            assertThat(result.getCountry()).isEqualTo("Kenya");
            assertThat(result.getOpenSource()).isEqualTo("No");
            assertThat(result.getHobby()).isEqualTo("Yes");
            assertThat(result.getRespondent()).isEqualTo(1);
        }
    }

    @Test
    void codingAsHobby() {
        String query = "select hobby,(count(Hobby)/(select count(*) from survey_results_public)) * 100 as percentage from survey_results_public group by Hobby";

        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)) {

            assertTimeout(Duration.ofMillis(100), () -> {
                jdbcTemplate.selectAll(this::hobbyMapper, query);
            });
        }
    }


    @Test
    void mission2() {
        String query = "select\n" +
                "a.devtype, tmp.average\n" +
                "from\n" +
                "(\n" +
                "select a.DevType_id, avg(b.yearsCodingProf) as average\n" +
                "from \n" +
                "yearsCodingProf_devtype as a\n" +
                "inner join\n" +
                "(select * from yearsCodingProf where yearsCodingProf != 'NA') as b\n" +
                "on a.Respondent = b.Respondent\n" +
                "group by a.devtype_id\n" +
                "order by average desc\n" +
                ") as tmp\n" +
                "Left Outer join\n" +
                "DevType as a\n" +
                "on tmp.devtype_id = a.id";

        try (JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)) {
            List<Hobby> hobbys = jdbcTemplate.selectAll(this::objectMapper, query);
            assertThat(hobbys.size()).isEqualTo(21);
            assertThat(hobbys.get(0).getResult()).isEqualTo("Engineering manager");
            assertTimeout(Duration.ofMillis(500), () -> {
                jdbcTemplate.selectAll(this::objectMapper, query);
            });
        }

    }


    private Result resultMapper(ResultSet rs) throws SQLException {
        return new Result(rs.getInt("Respondent"),
                rs.getString("hobby"),
                rs.getString("openSource"),
                rs.getString("country"));
    }

    private Hobby hobbyMapper(ResultSet rs) throws SQLException {
        return new Hobby(rs.getString("hobby"), rs.getDouble("percentage"));
    }


    private Hobby objectMapper(ResultSet rs) throws SQLException {
        return new Hobby(rs.getString("devtype"), rs.getDouble("average"));
    }
}
