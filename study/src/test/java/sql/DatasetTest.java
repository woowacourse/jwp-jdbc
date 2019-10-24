package sql;

import nextstep.jdbc.JdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import slipp.support.db.ConnectionManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DatasetTest {

    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource("src/test/java/sql/db.properties"));

    @Test
    @DisplayName("Coding As A Hobby")
    void findCodingAsHobby() {
        String sql = "SELECT round((count(*) * 100 / (SELECT count(*) FROM survey_results_public)), 1) AS percent" +
                " FROM survey_results_public" +
                " WHERE hobby = 'Yes'";

        Object o = jdbcTemplate.executeQueryWithUniqueResult(sql, rs -> rs.getObject("percent"))
                .orElseThrow(RuntimeException::new);
        assertThat((BigDecimal) o).isEqualTo("80.8");
    }

    @Test
    @DisplayName("Years of Professional Coding Experience by Developer Type")
    void findYearsOfProfessionalCoding() {
        String sqlByDevOpsSpecialist = createYearsOfProfessionalCodingQuery();
        List<ProfessionalCodingExperienceStatistics> statistics = jdbcTemplate.executeQuery(sqlByDevOpsSpecialist
                , rs -> new ProfessionalCodingExperienceStatistics(
                        rs.getString("devType"), rs.getDouble("average")
                ));

        Map<String, Double> results = modifyResultsToMap(statistics);
        assertThat(results.get("Engineering manager")).isEqualTo(10.2);
        assertThat(results.get("DevOps specialist")).isEqualTo(8.0);
        assertThat(results.get("Desktop or enterprise applications developer")).isEqualTo(7.7);
        assertThat(results.get("Embedded applications or devices developer")).isEqualTo(7.5);
    }

    private Map<String, Double> modifyResultsToMap(List<ProfessionalCodingExperienceStatistics> statistics) {
        Map<String, Double> results = new HashMap<>();
        for (ProfessionalCodingExperienceStatistics result : statistics) {
            results.put(result.getDevType(), result.getAverage());
        }
        return results;
    }

    private String createYearsOfProfessionalCodingQuery() {
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

    private class ProfessionalCodingExperienceStatistics {
        private String devType;
        private Double average;

        public ProfessionalCodingExperienceStatistics(final String devType, final Double average) {
            this.devType = devType;
            this.average = average;
        }

        public String getDevType() {
            return devType;
        }

        public Double getAverage() {
            return average;
        }

        @Override
        public String toString() {
            return "ProfessionalCodingExperienceStatistics{" +
                    "devType='" + devType + '\'' +
                    ", average=" + average +
                    '}';
        }
    }
}
