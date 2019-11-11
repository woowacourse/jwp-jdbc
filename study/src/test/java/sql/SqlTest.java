package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.util.List;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class SqlTest {
    private static final String propertiesPath = "src/test/resources/db.properties";

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource(propertiesPath));
    }

    @Test
    @DisplayName("Coding as hobby인 개발자의 비율")
    void coddingAsHobby() {
        String query = "select " +
                "round(selected.yes / selected.total, 3) as 'yes', " +
                "round((selected.total-selected.yes) / selected.total, 3) as 'no' " +
                "from (select count(*) as yes, (select count(*) from survey_results_public ) as total " +
                "from survey_results_public " +
                "where Hobby = 'yes') as selected;";

        String result = jdbcTemplate.executeQueryForObject(query,
                rs -> rs.getString("yes"))
                .orElseThrow(RuntimeException::new);
        assertThat(result).isEqualTo("0.808");
    }

    @Test
    @DisplayName("Hobby 성능 테스트")
    void codingAsHobbyTime() {
        String query = "select " +
                "round(selected.yes / selected.total, 2) as 'yes', " +
                "round((selected.total-selected.yes) / selected.total, 2) as 'no' " +
                "from (select count(*) as yes, (select count(*) from survey_results_public ) as total " +
                "from survey_results_public " +
                "where Hobby = 'yes') as selected;";

        assertTimeout(ofMillis(100), () -> {
            jdbcTemplate.executeQueryForObject(query,
                    rs -> rs.getString("yes"));
        });
    }

    @Test
    @DisplayName("DevType에 따른 professional coding experience 년차 평균")
    void professionalCodingExperience() {
        String query = createQuery();
        int engineeringManager = 0;
        int devOps = 3;
        int mobile = 17;
        int gameOrGraphics = 18;

        List<String> result = jdbcTemplate.executeQuery(query, rs -> rs.getString("average"));
        assertThat(result.get(engineeringManager)).isEqualTo("10.2");
        assertThat(result.get(devOps)).isEqualTo("8.0");
        assertThat(result.get(mobile)).isEqualTo("5.1");
        assertThat(result.get(gameOrGraphics)).isEqualTo("4.5");

    }

    private String createQuery() {
        return "SELECT t.devtype, round(avg(if(t.YearsCodingProf = '30 or more years', 30, substring_index(t.YearsCodingProf, '-', 1))), 1) AS average" +
                " from (SELECT" +
                " SUBSTRING_INDEX(SUBSTRING_INDEX(devtype, ';', n.digit + 1), ';', -1) devtype, YearsCodingProf" +
                " FROM" +
                " survey_results_public" +
                " INNER JOIN" +
                " (SELECT 0 digit UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) n" +
                " ON LENGTH(REPLACE(devtype, ';' , '')) <= LENGTH(devtype)-n.digit WHERE devtype != 'NA') as t" +
                " WHERE t.YearsCodingProf != 'NA'" +
                " group by t.devtype" +
                " order by average desc";
    }
}
