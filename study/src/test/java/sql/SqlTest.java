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

}
