package sql;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.PropertiesConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.support.db.DataBasePropertyReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTest {
    private static final Logger log = LoggerFactory.getLogger(SqlTest.class);

    private String path = "src/main/resources/db.properties";
    private JdbcTemplate jdbcTemplate;
    private DataBasePropertyReader dataBasePropertyReader;

    private String hobbyQuery = "SELECT HOBBY, ROUND((COUNT(HOBBY) / (SELECT COUNT(*) FROM survey_results_public)) * 100,1) 'RATIO'" +
            "FROM survey_results_public " +
            "GROUP BY hobby;";

    private String codingExperienceQuery = "select previous.job, round(avg(years),1) as coding_experience from (select\n" +
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

    @BeforeEach
    void setUp() {
        dataBasePropertyReader = new DataBasePropertyReader(path);
        PropertiesConnectionManager propertiesConnectionManager =
                new PropertiesConnectionManager(dataBasePropertyReader.getDriver(), dataBasePropertyReader.getUrl(),
                        dataBasePropertyReader.getUserName(), dataBasePropertyReader.getPassword());
        jdbcTemplate = new JdbcTemplate(propertiesConnectionManager);
        log.debug("{}", dataBasePropertyReader.getDriver());
    }

    @Test
    void hobby_설문결과() {
        List<HobbySurvey> results = jdbcTemplate.executeQueryThatHasResultSet(hobbyQuery, Collections.emptyList(),
                (resultSet) -> {
                    List<HobbySurvey> hobbySurveys = new ArrayList<HobbySurvey>();
                    while (resultSet.next()) {
                        HobbySurvey hobbySurvey =
                                new HobbySurvey(resultSet.getString("Hobby"),
                                        resultSet.getFloat("ratio"));
                        hobbySurveys.add(hobbySurvey);
                    }
                    return hobbySurveys;
                });

        assertThat(results.get(0).getHobby()).isEqualTo("No");
        assertThat(results.get(0).getRatio()).isEqualTo(19.2f);
        assertThat(results.get(1).getHobby()).isEqualTo("Yes");
        assertThat(results.get(1).getRatio()).isEqualTo(80.8f);
    }

    @Test
    void professional_coding_experience_설문결과() {

        List<CodingExperienceServey> results = jdbcTemplate.executeQueryThatHasResultSet(codingExperienceQuery, Collections.emptyList(),
                (resultSet) -> {
                    List<CodingExperienceServey> codingExperienceServeys = new ArrayList<CodingExperienceServey>();
                    while (resultSet.next()) {
                        CodingExperienceServey codingExperienceServey =
                                new CodingExperienceServey(resultSet.getString("job"),
                                        resultSet.getFloat("coding_experience"));
                        codingExperienceServeys.add(codingExperienceServey);
                    }
                    return codingExperienceServeys;
                });

        assertThat(results.get(0).getCodingExperience()).isEqualTo(10.2f);
        assertThat(results.get(3).getJob()).isEqualTo("DevOps specialist");
        assertThat(results.get(3).getCodingExperience()).isEqualTo(8f);
    }


}
