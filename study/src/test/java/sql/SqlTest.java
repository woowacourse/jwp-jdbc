package sql;

import nextstep.jdbc.ConnectionGenerator;
import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.DataBasePropertyReader;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {
    private String path = "/Users/jinwook/woowa_course/level3/jwp-jdbc/study/src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;
    private DataBasePropertyReader dataBasePropertyReader;

    private static final Logger logger = LoggerFactory.getLogger(SqlTest.class);
    @BeforeEach
    void setUp() {
        dataBasePropertyReader = new DataBasePropertyReader(path);
        dataBasePropertyReader.readDataBaseProperty();
        jdbcTemplate = new JdbcTemplate(
            ConnectionGenerator.getDataSource(dataBasePropertyReader.getDriver(), dataBasePropertyReader.getUrl(),
                dataBasePropertyReader.getUserName(), dataBasePropertyReader.getPassword()));
        logger.debug("{}",dataBasePropertyReader.getDriver());
    }

    @Test
    void hobby_설문결과() {
        String sql = "SELECT HOBBY, ROUND((COUNT(HOBBY) / (SELECT COUNT(*) FROM survey_results_public)) * 100,1) 'RATIO'" +
            "FROM survey_results_public " +
            "GROUP BY hobby;";
        List<HobbySurvey> hobbySurveys = jdbcTemplate.listQuery(sql,
                rs -> new HobbySurvey(rs.getString("Hobby"), rs.getFloat("ratio")));
        assertThat(hobbySurveys.get(0).getHobby()).isEqualTo("No");
        assertThat(hobbySurveys.get(0).getRatio()).isEqualTo(19.2f);
        assertThat(hobbySurveys.get(1).getHobby()).isEqualTo("Yes");
        assertThat(hobbySurveys.get(1).getRatio()).isEqualTo(80.8f);
    }

    @Test
    void professional_coding_experience_설문결과() {
        String sql = "select previous.job, round(avg(years),1) as coding_experience from (select\n" +
                "  survey_results_public.Respondent,\n" +
                "  SUBSTRING_INDEX(survey_results_public.YearsCodingProf,'-',1) as years,\n" +
                "  SUBSTRING_INDEX(SUBSTRING_INDEX(survey_results_public.DevType,';', numbers.n), ';', -1) job\n" +
                "from\n" +
                "  (select 1 n union all\n" +
                "   select 2 union all select 3 union all\n" +
                "   select 4 union all select 5 union all\n" +
                "   select 6 union all select 7 union all\n" +
                "   select 8 union all select 9 union all\n" +
                "   select 10 union all select 11 union all\n" +
                "   select 12 union all select 13 union all\n" +
                "   select 14 union all select 15 union all\n" +
                "   select 16 union all select 17 union all\n" +
                "   select 18 union all select 19\n" +
                "   ) numbers INNER JOIN survey_results_public\n" +
                "  on CHAR_LENGTH(survey_results_public.DevType)\n" +
                "     -CHAR_LENGTH(REPLACE(survey_results_public.DevType, ';', ''))>=numbers.n-1\n" +
                "     where survey_results_public.YearsCodingProf != 'NA'\n" +
                "order by\n" +
                "  Respondent, n) previous\n" +
                "  group by previous.job\n" +
                "  order by avg(years) desc;";

        List<CodingExperienceServey> hobbySurveys = jdbcTemplate.listQuery(sql,
                rs -> new CodingExperienceServey(rs.getString("job"), rs.getFloat("coding_experience")));
        assertThat(hobbySurveys.get(0).getJob()).isEqualTo("Engineering manager");
        assertThat(hobbySurveys.get(0).getCodingExperience()).isEqualTo(10.2f);
        assertThat(hobbySurveys.get(3).getJob()).isEqualTo("DevOps specialist");
        assertThat(hobbySurveys.get(3).getCodingExperience()).isEqualTo(8f);

    }

    @Test
    void connection() {
        assertTimeout(Duration.ofMillis(1), () -> {
            ConnectionGenerator.getConnection("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:13306/jwp_jdbc?useUnicode=true&characterEncoding=utf8",
                    "techcourse", "password");
        });
    }

    @Test
    void dataSource() {
        assertTimeout(Duration.ofMillis(1), () -> {
            ConnectionGenerator.getDataSource("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:13306/jwp_jdbc?useUnicode=true&characterEncoding=utf8",
                    "techcourse", "password").getConnection();
        });
    }
}
